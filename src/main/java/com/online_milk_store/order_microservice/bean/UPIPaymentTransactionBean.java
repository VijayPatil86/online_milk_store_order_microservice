package com.online_milk_store.order_microservice.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class UPIPaymentTransactionBean {
	private String orderNumber;
	private String upiId;
	private String upiPaymentMethod;
	private String upiPaymentStatus;
	private String upiPaymentRemark;
}
