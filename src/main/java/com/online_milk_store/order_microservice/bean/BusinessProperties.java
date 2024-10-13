package com.online_milk_store.order_microservice.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@PropertySource("classpath:business-props.properties")
public class BusinessProperties {
	@Value("${upi.payment.method.name}")
	private String upiPaymentMethodName;

	@Value("${upi.payment.transaction.initial.status}")
	private String upiPaymentTransactionInitialStatus;
}
