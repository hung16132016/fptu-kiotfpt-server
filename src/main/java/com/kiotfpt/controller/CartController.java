package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.CartService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/cart")
public class CartController {

	@Autowired
	private CartService service;

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("")
	public ResponseEntity<ResponseObject> getCartByID() {
		return service.getCartByID();
	}

	@GetMapping("/amount")
	public ResponseEntity<ResponseObject> getAmountCartByAccountID(@RequestParam(name = "accountID") int accountID) {
		return service.getAmountCartByAccountID(accountID);
	}

	@DeleteMapping("/delete-all/{cartId}")
	public ResponseEntity<ResponseObject> deleteAllSectionsInCart(@PathVariable int cartId) {
		return service.deleteSectionsByStatusInCart(cartId);
	}
}
