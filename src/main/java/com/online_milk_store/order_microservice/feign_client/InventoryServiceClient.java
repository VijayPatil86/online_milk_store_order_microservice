package com.online_milk_store.order_microservice.feign_client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", path = "/inventory-service/inventory")
public interface InventoryServiceClient {
	@GetMapping("/milk-brand")
	ResponseEntity<Map<String, Object>> checkInventoryMilkBrands(@RequestParam Map<String, String> queryParams);
}
