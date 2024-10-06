package com.online_milk_store.order_microservice.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Order {
	private String productIdQty;	// 21=1&22=2
	private PaymentDetails paymentDetails;

	@Override
	public String toString() {
		return "Order [productIdQty=" + productIdQty + ", paymentDetails=" + paymentDetails + "]";
	}
}
