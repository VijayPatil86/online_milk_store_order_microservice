package com.online_milk_store.order_microservice.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.order_microservice.feign_client.InventoryServiceClient;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/order-service")
public class OrderController {
	static final private Logger LOGGER = LogManager.getLogger(OrderController.class);

	@Autowired
	private InventoryServiceClient inventoryServiceClient;

	@GetMapping("/milk-brand")
	public ResponseEntity<Map<String, Object>> checkInventoryMilkBrands(@RequestParam Map<String, String> mapQueryParams,
			HttpServletRequest request) {
		LOGGER.debug("OrderController.checkInventoryMilkBrands() --- START");
		LOGGER.info("OrderController.checkInventoryMilkBrands() --- mapQueryParams: " + mapQueryParams);

		ResponseEntity<Map<String, Object>> responseInventoryMilkBrands =
				inventoryServiceClient.checkInventoryMilkBrands(mapQueryParams);
		LOGGER.info("OrderController.checkInventoryMilkBrands() --- responseInventoryMilkBrands: " + responseInventoryMilkBrands.getBody());
		Map<String, Object> mapResponse = responseInventoryMilkBrands.getBody();
		mapResponse.put("order service port: ", request.getLocalPort());

		LOGGER.debug("OrderController.checkInventoryMilkBrands() --- END");
		return responseInventoryMilkBrands;
	}
}
