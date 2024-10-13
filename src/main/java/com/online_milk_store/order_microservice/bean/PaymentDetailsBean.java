package com.online_milk_store.order_microservice.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentDetailsBean {
	PaymentMethodBean paymentMethodBean;

	@Override
	public String toString() {
		return "PaymentDetails [paymentMethodBean=" + paymentMethodBean + "]";
	}
}
