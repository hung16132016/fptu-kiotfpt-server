package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.kiotfpt.request.StatusRequest;
import com.kiotfpt.service.OrderService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/v1")
public class OrderController {

	@Autowired
	private OrderService service;

	@GetMapping("/order/get-all")
	public ResponseEntity<ResponseObject> getAllOrderByAccountID(@RequestParam(name = "accountID") int id) {
		return service.getOrderByAccountID(id);
	}

	@GetMapping("/order/get-by-shop")
	public ResponseEntity<ResponseObject> getOrderByShop(@RequestParam(name = "shop_id") Integer shopID,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int amount) {
		return service.getOrderByShopID(shopID, page, amount);
	}

	@PutMapping("/order/update/{id}")
	public ResponseEntity<ResponseObject> updateOrder(@PathVariable int id, @RequestBody StatusRequest status) throws JsonProcessingException {
		return service.updateOrderStatus(id, status);
	}

	@PutMapping("/order/delete/{id}")
	public ResponseEntity<ResponseObject> deleteOrder(@PathVariable int id) throws JsonProcessingException {
		return service.updateOrderStatus(id, new StatusRequest(1, "Inactive"));
	}

	@PostMapping("/checkout")
	public ResponseEntity<ResponseObject> createOrder(@RequestBody CreateOrderRequest map) {
		return service.createOrder(map);
	}

	@GetMapping("/order/get-current")
	public ResponseEntity<ResponseObject> getCurrentOrders(
			@RequestParam(name = "account_id", required = true) Integer id) {
		return service.getCurrentOrders(id);
	}

}
