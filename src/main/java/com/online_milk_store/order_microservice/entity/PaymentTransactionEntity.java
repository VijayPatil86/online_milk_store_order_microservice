package com.online_milk_store.order_microservice.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "PAYMENT_TRANSACTION")
public class PaymentTransactionEntity {
	@Column(name = "TRANSACTION_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transactionId;

	@Column(name = "ORDER_NUMBER")
	private String orderNumber;

	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;

	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;

	@Column(name = "CUSTOMER_BANK_TXN_ID")
	private String custBankTxnId;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "UPI_RECORD_ID")
	private UPIDetailsEntity upiDetailsEntity;

	@ManyToOne
	@JoinColumn(name = "MILK_BRAND_SELL_DETAIL_ID")
	private MilkBrandSellEntity milkBrandSellEntity;

	@Column(name = "REMARK")
	private String upiPaymentRemark;
}
