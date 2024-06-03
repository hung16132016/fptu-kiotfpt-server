package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
