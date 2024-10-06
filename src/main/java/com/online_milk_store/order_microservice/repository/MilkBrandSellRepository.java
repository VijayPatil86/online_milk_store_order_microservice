package com.online_milk_store.order_microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.order_microservice.entity.MilkBrandSellEntity;

public interface MilkBrandSellRepository extends JpaRepository<MilkBrandSellEntity, Integer> {

}
