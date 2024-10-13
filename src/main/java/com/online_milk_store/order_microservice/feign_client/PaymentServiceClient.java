package com.online_milk_store.order_microservice.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online_milk_store.order_microservice.bean.UPIPaymentTransactionBean;

@FeignClient(name = "payment-service", url = "http://localhost:9041/payment-service")
public interface PaymentServiceClient {
	@PostMapping("/upi-payment")
	ResponseEntity<Void> processUPIPayment(@RequestBody UPIPaymentTransactionBean upiPaymentTransactionBean);
}
