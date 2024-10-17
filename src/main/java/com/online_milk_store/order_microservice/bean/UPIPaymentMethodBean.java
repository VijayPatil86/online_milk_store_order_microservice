package com.online_milk_store.order_microservice.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UPIPaymentMethodBean extends PaymentMethodBean {
	private String upiAddress;
	private String paymentDescription;

	@Override
	public String toString() {
		return "UPIPaymentMethod [upiAddress=" + upiAddress + ", paymentDescription=" + paymentDescription + "]";
	}
}
