package com.kiotfpt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.ShopService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1")
@Service
public class ShopController {
	@Autowired
	private ShopService service;

	@GetMapping("/shop/{id}")
	public ResponseEntity<ResponseObject> getShopByID(HttpServletRequest request, @PathVariable int id) {
		return service.getShopByID(request, id);
	}
	
//	@GetMapping("/shop")
//	public ResponseEntity<ResponseObject> getAllShop(HttpServletRequest request) {
//		return service.getAllShop(request);
//	}
//
//	@GetMapping("/shop/account/{id}")
//	public ResponseEntity<ResponseObject> getShopByAccountID(HttpServletRequest request, @PathVariable int id) {
//		return service.getShopByAccountID(request, id);
//	}
//
//	@PostMapping("/shop/create")
//	public ResponseEntity<ResponseObject> createShop(@RequestBody Map<String, String> shop) {
//		return service.createShop(shop);
//	}
//
//	@PutMapping("/shop/update/{id}")
//	public ResponseEntity<ResponseObject> updateShop(HttpServletRequest request, @PathVariable int id,
//			@RequestBody Shop shop) {
//		return service.updateShop(request, id, shop);
//	}
//	@GetMapping("/shop/account/getAll")
//	public ResponseEntity<ResponseObject> getAllShopRevenueByTime(HttpServletRequest request, @RequestParam(value = "filter", required = false) int month) {		
//		return service.getAllShopRevenueByTime(request, month);
//	}
//
//	@GetMapping("/shop/account/getTotalRevenue")
//	public ResponseEntity<ResponseObject> getTotalRevenueByTime(HttpServletRequest request,@RequestParam(value = "filter", required = false) int month) {		
//		return service.getTotalRevenueByTime(request, month);
//	}
//	
//	@GetMapping("/shop/getTotalRevenueByYear")
//	public ResponseEntity<ResponseObject> getTotalRevenueByYear(HttpServletRequest request,@RequestParam(value = "filter", required = false) int year) {		
//		return service.getTotalRevenueByYear(request, year);
//	}

}
