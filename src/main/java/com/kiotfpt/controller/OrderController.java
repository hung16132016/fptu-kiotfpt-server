package com.kiotfpt.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.SectionService;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/api/v1/order")
public class OrderController {

	@Autowired
	private SectionService service;

	@GetMapping("/get-by")
	public ResponseEntity<ResponseObject> getAllSubOrder(@RequestParam(name= "shop_id") int shop_id) {
		return service.getByShopID(shop_id);
	}

//	@PostMapping("/create")
//	public ResponseEntity<ResponseObject> createSubOrder(@RequestBody Map<String, String> map) {
//		return service.createOrder(map);
//	}
 
	@PutMapping("/update-status/{id}")
	public ResponseEntity<ResponseObject> updateSubOrder(@PathVariable("id") int order_id, @RequestBody Map<String, String> map) {
		return service.updateStatusOrder(order_id, map);
	}
}
