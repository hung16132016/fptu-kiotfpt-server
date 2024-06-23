package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.ItemRequest;
import com.kiotfpt.service.AccessibilityItemService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1")
public class AccessibilityItemController {
	@Autowired
	private AccessibilityItemService service;

	@PostMapping("/add-to-cart")
	public ResponseEntity<ResponseObject> addProductToCart(@RequestBody ItemRequest item) {
		return service.createItem(item);
	}

	@DeleteMapping("/item/delete/{id}")
	public ResponseEntity<ResponseObject> deleteItem(@PathVariable int id) {
		return service.deleteItem(id);
	}

	@GetMapping("item/update-amount")
	public ResponseEntity<ResponseObject> updateItemAmount(@RequestParam int itemId, @RequestParam int newAmount) {
		return service.updateItemAmount(itemId, newAmount);
	}
}
