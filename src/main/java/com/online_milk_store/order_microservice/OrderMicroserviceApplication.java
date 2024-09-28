package com.online_milk_store.order_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OrderMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMicroserviceApplication.class, args);
		System.out.println("success... order service...");
	}

}
