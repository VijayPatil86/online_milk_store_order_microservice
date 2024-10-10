package com.online_milk_store.order_microservice.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.order_microservice.bean.Order;
import com.online_milk_store.order_microservice.bean.UPIPaymentMethod;
import com.online_milk_store.order_microservice.entity.MilkBrandInventoryEntity;
import com.online_milk_store.order_microservice.entity.MilkBrandSellEntity;
import com.online_milk_store.order_microservice.entity.UPIDetailsEntity;
import com.online_milk_store.order_microservice.repository.MilkBrandInventoryRepository;
import com.online_milk_store.order_microservice.repository.MilkBrandSellRepository;
import com.online_milk_store.order_microservice.repository.UPIDetailRepository;
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
	private UPIDetailRepository upiDetailRepository;

	@Autowired
	private Util util;

	public void processOrder(Order order) {
		LOGGER.debug("OrderService.processOrder() --- START");
		LOGGER.info("OrderService.processOrder() --- order: " + order);	// Order [productIdQty=21=2&22=4, paymentDetails=PaymentDetails [paymentMethod=UPIPaymentMethod [upiID=acc@hdfc, paymentDescription=buy milk piuch]]]
		UPIDetailsEntity upiDetailsEntitySaved = getOrSaveUPIDetailsEntity(order);
		Map<Integer, Integer> mapProductIdQuantity = new HashMap<>();
		List<MilkBrandInventoryEntity> listMilkBrandInventoryEntities = getMilkBrandIdsForOrder(order, mapProductIdQuantity);
		saveMilkOrder(listMilkBrandInventoryEntities, mapProductIdQuantity, upiDetailsEntitySaved, order);
		LOGGER.debug("OrderService.processOrder() --- END");
	}

	private UPIDetailsEntity getOrSaveUPIDetailsEntity(Order order) {
		LOGGER.debug("OrderService.getOrSaveUPIDetailsEntity() --- START");
		String upiId = ((UPIPaymentMethod)(order.getPaymentDetails().getPaymentMethod())).getUpiID();
		Optional<UPIDetailsEntity> optionalUPIDetailsEntity = upiDetailRepository.findByUpiId(upiId);
		UPIDetailsEntity upiDetailsEntitySaved = optionalUPIDetailsEntity.orElseGet(() -> upiDetailRepository
				.save(UPIDetailsEntity.builder()
						.upiId(upiId)
						.build()));
		LOGGER.debug("OrderService.getOrSaveUPIDetailsEntity() --- END");
		return upiDetailsEntitySaved;
	}

	private List<MilkBrandInventoryEntity> getMilkBrandIdsForOrder(Order order, Map<Integer, Integer> mapProductIdQuantity) {
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
			Map<Integer, Integer> mapProductIdQuantity, UPIDetailsEntity upiDetailsEntitySaved,
			Order order) {
		LOGGER.debug("OrderService.saveMilkOrder() --- START");
		String remark = ((UPIPaymentMethod)(order.getPaymentDetails().getPaymentMethod())).getPaymentDescription();
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
						.remark(remark)
						.upiDetailsEntity(upiDetailsEntitySaved)
						.build())
				.toList();
		List<MilkBrandSellEntity> listMilkBrandSellEntitiesToSaved = milkBrandSellRepository.saveAll(listMilkBrandSellEntitiesToSave);
		LOGGER.debug("OrderService.saveMilkOrder() --- END");
	}
}
