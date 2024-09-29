package com.online_milk_store.order_microservice.feign_client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "inventory-service", path = "/inventory-service/inventory")
public interface InventoryServiceClient {
}
