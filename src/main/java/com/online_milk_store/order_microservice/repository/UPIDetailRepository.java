package com.online_milk_store.order_microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.order_microservice.entity.UPIDetailsEntity;

public interface UPIDetailRepository extends JpaRepository<UPIDetailsEntity, Integer> {
	//Optional<UPIDetailsEntity> findByUpiId(String upiId);
}
