package com.online_milk_store.order_microservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.order_microservice.bean.Order;
import com.online_milk_store.order_microservice.entity.MilkBrandInventoryEntity;
import com.online_milk_store.order_microservice.repository.MilkBrandInventoryRepository;
import com.online_milk_store.order_microservice.repository.MilkBrandSellRepository;

@Service
@Transactional
public class OrderService {
	static final private Logger LOGGER = LogManager.getLogger(OrderService.class);

	@Autowired
	private MilkBrandSellRepository milkBrandSellRepository;

	@Autowired
	private MilkBrandInventoryRepository milkBrandInventoryRepository;

	public void processOrder(Order order) {
		LOGGER.debug("OrderService.processOrder() --- START");
		LOGGER.info("OrderService.processOrder() --- order: " + order);	// Order [productIdQty=21=2&22=4, paymentDetails=PaymentDetails [paymentMethod=UPIPaymentMethod [upiID=acc@hdfc, paymentDescription=buy milk piuch]]]
		List<Integer> productIds = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(order.getProductIdQty(), "&");
		while(tokenizer.hasMoreTokens()) {
			String pairProductIdQuantity = tokenizer.nextToken();
			int equalsPosition = pairProductIdQuantity.indexOf("=");
			if(equalsPosition != -1) {
				int productId = Integer.parseInt(pairProductIdQuantity.substring(0, equalsPosition));
				productIds.add(productId);
			}
		}
		List<MilkBrandInventoryEntity> listMilkBrandInventoryEntities =
				milkBrandInventoryRepository.findByMilkBrandEntityMilkBrandIdIn(productIds);
		LOGGER.debug("OrderService.processOrder() --- END");
	}
}
