package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.CartService;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/cart")
@Service
public class CartController {

	@Autowired
	private CartService service;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getCartByID(@PathVariable int id) {
		return service.getCartByID(id);
	}
	
	@GetMapping("/amount")
	public ResponseEntity<ResponseObject> getAmountCartByAccountID(@RequestParam(name = "accountID") int accountID) {
		return service.getAmountCartByAccountID(accountID);
	}

}
