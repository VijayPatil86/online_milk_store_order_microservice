package com.online_milk_store.order_microservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.order_microservice.feign_client.InventoryServiceClient;

@CrossOrigin
@RestController
@RequestMapping("/order-service")
public class OrderController {
	static final private Logger LOGGER = LogManager.getLogger(OrderController.class);

	@Autowired
	private InventoryServiceClient inventoryServiceClient;
}
