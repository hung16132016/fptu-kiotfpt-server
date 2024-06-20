package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.DateRequest;
import com.kiotfpt.service.OrderService;
import com.kiotfpt.service.ProductService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/statis")
public class StatisController {

	@Autowired
	private OrderService orderService;
	

	@Autowired
	private ProductService productService;

	@PostMapping("/order/filter-by-shop")
	public ResponseEntity<ResponseObject> filterOrdersByTimeAndShop(@RequestBody DateRequest filterRequest,
			@RequestParam int shopId) {

		return orderService.filterOrdersByTimeAndShop(filterRequest, shopId);
	}

	@PostMapping("/order/filter")
	public ResponseEntity<ResponseObject> filterOrdersByTime(@RequestBody DateRequest filterRequest) {

		return orderService.filterOrdersByTime(filterRequest);
	}
	
	@PostMapping("/product/filter")
	public ResponseEntity<ResponseObject> filterProductsByTime(@RequestBody DateRequest filterRequest) {

		return productService.filterProductsByTime(filterRequest);
	}
	
	@PostMapping("/product/filter-by-shop")
	public ResponseEntity<ResponseObject> filterProductsByTimeAndShop(@RequestBody DateRequest filterRequest,
			@RequestParam int shopId) {

		return productService.filterProductsByTimeAndShop(filterRequest, shopId);
	}

}
