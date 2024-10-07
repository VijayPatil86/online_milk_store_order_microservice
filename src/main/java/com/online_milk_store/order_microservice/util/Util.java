package com.online_milk_store.order_microservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Util {
	public String generateOrderNumber() {
		Random random = new Random();
		Date date = new Date();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
		String orderNumber = "ORD-" + timestamp + "-" + random.nextInt(1, 100000);
		date = null;
		return orderNumber;
	}
}
