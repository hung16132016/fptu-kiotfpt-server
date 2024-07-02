package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.DateRequest;
import com.kiotfpt.service.AccountProfileService;
import com.kiotfpt.service.OrderService;
import com.kiotfpt.service.ProductService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/statis")
public class StatisController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private AccountProfileService profileService;

	@Autowired
	private ProductService productService;

	@PreAuthorize("hasAnyAuthority('admin', 'shop')")
	@PostMapping("/order")
	public ResponseEntity<ResponseObject> filterOrdersByTime(@RequestBody DateRequest filterRequest) {

		return orderService.filterOrdersByTime(filterRequest);
	}

	@PreAuthorize("hasAnyAuthority('admin', 'shop')")
	@PostMapping("/revenue")
	public ResponseEntity<ResponseObject> revenue(@RequestBody DateRequest filterRequest) {

		return orderService.revenue(filterRequest);
	}

	@PreAuthorize("hasAnyAuthority('admin', 'shop')")
	@PostMapping("/product")
	public ResponseEntity<ResponseObject> filterProductsByTimeAndShop(@RequestBody DateRequest filterRequest) {

		return productService.filterProductsByTimeAndShop(filterRequest);
	}

	@GetMapping("/customer")
	public ResponseEntity<ResponseObject> sortAccountByTotalSpent(@RequestParam int shopId) {

		return profileService.getProfilesOrderedByTotalSpent(shopId);
	}

	@GetMapping("/reviews")
	public ResponseEntity<ResponseObject> getProductsWithReviews() {
		return productService.getProductsWithReviews();
	}
}
