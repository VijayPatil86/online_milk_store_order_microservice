package com.online_milk_store.order_microservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.order_microservice.entity.MilkBrandInventoryEntity;

public interface MilkBrandInventoryRepository extends JpaRepository<MilkBrandInventoryEntity, Integer> {
	List<MilkBrandInventoryEntity> findByMilkBrandEntityMilkBrandIdIn(List<Integer> milkBrandId);
}
