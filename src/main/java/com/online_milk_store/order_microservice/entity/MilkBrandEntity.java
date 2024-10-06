package com.online_milk_store.order_microservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "MILK_BRAND")
public class MilkBrandEntity {
	@Column(name = "MILK_BRAND_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int milkBrandId;

	@Column(name = "MILK_BRAND_NAME")
	private String milkBrandName;

	@Column(name = "PACKAGING")
	private String packaging;

	@Column(name = "MILK_BRAND_AVAILABLE")
	private String milkBrandAvailable;

	@Override
	public String toString() {
		return "MilkBrandEntity [milkBrandId=" + milkBrandId + ", milkBrandName=" + milkBrandName + ", packaging="
				+ packaging + ", milkBrandAvailable=" + milkBrandAvailable + "]";
	}
}
