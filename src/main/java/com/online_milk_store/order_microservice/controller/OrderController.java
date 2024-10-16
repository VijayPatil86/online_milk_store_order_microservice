package com.online_milk_store.order_microservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.order_microservice.bean.OrderBean;
import com.online_milk_store.order_microservice.bean.UPIPaymentTransactionBean;
import com.online_milk_store.order_microservice.feign_client.PaymentServiceClient;
import com.online_milk_store.order_microservice.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping("/order-service/order")
public class OrderController {
	static final private Logger LOGGER = LogManager.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentServiceClient paymentServiceClient;

	@PostMapping
	public ResponseEntity<String> processOrder(@RequestBody OrderBean orderBean) {
		LOGGER.debug("OrderController.processOrder() --- START");
		LOGGER.info("OrderController.processOrder() --- orderBean: " + orderBean);	// Order [productIdQty=21=2&22=4, paymentDetails=PaymentDetails [paymentMethod=UPIPaymentMethod [upiID=acc@hdfc, paymentDescription=buy milk piuch]]]
		UPIPaymentTransactionBean upiPaymentTransactionBean = orderService.createOrder(orderBean);
		ResponseEntity<Void> responseEntity = paymentServiceClient.processUPIPayment(upiPaymentTransactionBean);
		LOGGER.debug("OrderController.processOrder() --- END");
		return new ResponseEntity<>("Order processed...", HttpStatus.OK);
	}
}
