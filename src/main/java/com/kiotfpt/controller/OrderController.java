package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.CreateOrderRequest;
import com.kiotfpt.service.OrderService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/order")
public class OrderController {

	@Autowired
	private OrderService service;

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllOrderByAccountID() {
		return service.getOrderByAccountID();
	}

	@PreAuthorize("hasAuthority('shop')")
	@GetMapping("/get-by-shop")
	public ResponseEntity<ResponseObject> getOrderByShop(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int amount) {
		return service.getOrderByShopID(page, amount);
	}

	@PreAuthorize("hasAnyAuthority('shop', 'user')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseObject> updateOrder(@PathVariable int id, @RequestParam String status)
			throws JsonProcessingException {
		return service.updateOrderStatus(id, status);
	}

	@PutMapping("/delete/{id}")
	public ResponseEntity<ResponseObject> deleteOrder(@PathVariable int id) throws JsonProcessingException {
		return service.updateOrderStatus(id, "inactive");
	}

	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/checkout")
	public ResponseEntity<ResponseObject> createOrder(@RequestBody CreateOrderRequest map) {
		return service.createOrder(map);
	}

	@GetMapping("/get-current")
	public ResponseEntity<ResponseObject> getCurrentOrders(
			@RequestParam(name = "account_id", required = true) Integer id) {
		return service.getCurrentOrders(id);
	}
	
	@GetMapping("/update-pay")
	public ResponseEntity<ResponseObject> updateStatPay(
			@PathVariable int id) {
		return service.updateOrderStatusPay(id);
	}

}
