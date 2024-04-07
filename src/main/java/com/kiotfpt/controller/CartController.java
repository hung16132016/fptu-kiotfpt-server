package com.kiotfpt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.CartService;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/cart")
@Service
public class CartController {

	@Autowired
	private CartService service;

	@GetMapping("")
	public ResponseEntity<ResponseObject> getOrderByAccount(@RequestParam(value = "accountID", required = false) int id,
			HttpServletRequest request) {
		return service.getCartByAccount(id);
	}
//
//	@PostMapping("/create")
//	public ResponseEntity<ResponseObject> createOrder(@RequestBody Map<String, String> map) {
//		return service.createOrder(map);
//	}
//
//	@PutMapping("/update")
//	public ResponseEntity<ResponseObject> updateOrder(@RequestBody Cart cart) {
//		return service.updateOrder(cart);
//	}
}
