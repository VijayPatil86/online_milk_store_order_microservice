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
@Table(name = "MILK_BRAND_SELL_DETAILS")
public class MilkBrandSellEntity {
	@Column(name = "DETAIL_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int detailId;

	@Column(name = "MILK_BRAND_SELL_PRICE")
	private float sellPrice;

	@Column(name = "MILK_BRAND_SELL_QUANTITY")
	private int sellQuantity;

	@Column(name = "MILK_BRAND_TOTAL_SELL_PRICE")
	private float totalSellPrice;

	@Column(name = "SELL_TIMESTAMP")
	private Timestamp sellDateTime;

	@Column(name = "ORDER_NUMBER")
	private String orderNumber;

	@ManyToOne
	@JoinColumn(name = "MILK_BRAND_ID")
	private MilkBrandEntity milkBrandEntity;

	@ManyToOne
	@JoinColumn(name = "UPI_RECORD_ID")
	private UPIDetailsEntity upiDetailsEntity;
}
