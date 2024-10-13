package com.online_milk_store.order_microservice.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.order_microservice.bean.BusinessProperties;
import com.online_milk_store.order_microservice.bean.OrderBean;
import com.online_milk_store.order_microservice.bean.UPIPaymentMethodBean;
import com.online_milk_store.order_microservice.bean.UPIPaymentTransactionBean;
import com.online_milk_store.order_microservice.entity.MilkBrandInventoryEntity;
import com.online_milk_store.order_microservice.entity.MilkBrandSellEntity;
import com.online_milk_store.order_microservice.feign_client.PaymentServiceClient;
import com.online_milk_store.order_microservice.repository.MilkBrandInventoryRepository;
import com.online_milk_store.order_microservice.repository.MilkBrandSellRepository;
import com.online_milk_store.order_microservice.util.Util;

@Service
@Transactional
public class OrderService {
	static final private Logger LOGGER = LogManager.getLogger(OrderService.class);

	@Autowired
	private MilkBrandSellRepository milkBrandSellRepository;

	@Autowired
	private MilkBrandInventoryRepository milkBrandInventoryRepository;

	@Autowired
	private PaymentServiceClient paymentServiceClient;

	@Autowired
	private Util util;

	@Autowired
	private BusinessProperties businessProperties;

	public void processOrder(OrderBean orderBean) {
		LOGGER.debug("OrderService.processOrder() --- START");
		LOGGER.info("OrderService.processOrder() --- orderBean: " + orderBean);	// Order [productIdQty=21=2&22=4, paymentDetails=PaymentDetails [paymentMethod=UPIPaymentMethod [upiID=acc@hdfc, paymentDescription=buy milk piuch]]]
		Map<Integer, Integer> mapProductIdQuantity = new HashMap<>();
		List<MilkBrandInventoryEntity> listMilkBrandInventoryEntities = getMilkBrandIdsForOrder(orderBean, mapProductIdQuantity);
		saveMilkOrder(listMilkBrandInventoryEntities, mapProductIdQuantity, orderBean);
		LOGGER.debug("OrderService.processOrder() --- END");
	}

	private List<MilkBrandInventoryEntity> getMilkBrandIdsForOrder(OrderBean order, Map<Integer, Integer> mapProductIdQuantity) {
		LOGGER.debug("OrderService.getMilkBrandIds() --- START");
		LOGGER.info("OrderService.getMilkBrandIds() --- order: " + order);
		StringTokenizer tokenizer = new StringTokenizer(order.getProductIdQty(), "&");
		while(tokenizer.hasMoreTokens()) {
			String pairProductIdQuantity = tokenizer.nextToken();
			int equalsPosition = pairProductIdQuantity.indexOf("=");
			if(equalsPosition != -1) {
				int productId = Integer.parseInt(pairProductIdQuantity.substring(0, equalsPosition));
				int productQuantity = Integer.parseInt(pairProductIdQuantity.substring(equalsPosition + 1, pairProductIdQuantity.length()));
				mapProductIdQuantity.put(productId, productQuantity);
			}
		}
		List<MilkBrandInventoryEntity> listMilkBrandInventoryEntities =
				milkBrandInventoryRepository.findByMilkBrandEntityMilkBrandIdIn(new ArrayList<>(mapProductIdQuantity.keySet()));
		LOGGER.debug("OrderService.getMilkBrandIds() --- END");
		return listMilkBrandInventoryEntities;
	}

	private void saveMilkOrder(List<MilkBrandInventoryEntity> listMilkBrandInventoryEntities,
			Map<Integer, Integer> mapProductIdQuantity, OrderBean orderBean) {
		LOGGER.debug("OrderService.saveMilkOrder() --- START");
		Map<Integer, MilkBrandInventoryEntity> mapMilkBrandIdMilkBrandInventoryEntity = listMilkBrandInventoryEntities.stream()
                .collect(Collectors.toMap(
                milkBrandInventoryEntity -> milkBrandInventoryEntity.getMilkBrandEntity().getMilkBrandId(),
                milkBrandInventoryEntity -> milkBrandInventoryEntity
                ));
		String orderNumber = util.generateOrderNumber();
		List<MilkBrandSellEntity> listMilkBrandSellEntitiesToSave = mapProductIdQuantity.entrySet().stream()
				.map(entryProductIdQuantity ->
					MilkBrandSellEntity.builder()
						.sellPrice(mapMilkBrandIdMilkBrandInventoryEntity.get(entryProductIdQuantity.getKey()).getCurrentSellPrice())
						.sellQuantity(entryProductIdQuantity.getValue())
						.totalSellPrice(entryProductIdQuantity.getValue() * mapMilkBrandIdMilkBrandInventoryEntity.get(entryProductIdQuantity.getKey()).getCurrentSellPrice())
						.milkBrandEntity(mapMilkBrandIdMilkBrandInventoryEntity.get(entryProductIdQuantity.getKey()).getMilkBrandEntity())
						.sellDateTime(new Timestamp(System.currentTimeMillis()))
						.orderNumber(orderNumber)
						.build())
				.toList();
		List<MilkBrandSellEntity> listMilkBrandSellEntitiesToSaved = milkBrandSellRepository.saveAll(listMilkBrandSellEntitiesToSave);
		UPIPaymentTransactionBean upiPaymentTransactionBean = UPIPaymentTransactionBean.builder()
				.orderNumber(orderNumber)
				.upiPaymentMethod(businessProperties.getUpiPaymentMethodName())
				.upiPaymentStatus(businessProperties.getUpiPaymentTransactionInitialStatus())
				.upiId(((UPIPaymentMethodBean)(orderBean.getPaymentDetails().getPaymentMethod())).getUpiID())
				.upiPaymentRemark(((UPIPaymentMethodBean)(orderBean.getPaymentDetails().getPaymentMethod())).getPaymentDescription())
				.build();
		ResponseEntity<Void> responseEntity = paymentServiceClient.processUPIPayment(upiPaymentTransactionBean);
		LOGGER.debug("OrderService.saveMilkOrder() --- END");
	}
}
