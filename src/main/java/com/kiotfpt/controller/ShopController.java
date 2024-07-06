package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.ShopRequest;
import com.kiotfpt.service.ShopService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/shop")
public class ShopController {
	@Autowired
	private ShopService service;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getShopByID(@PathVariable int id) {
		return service.getShopByID(id);
	}

	@PreAuthorize("hasAuthority('shop')")
	@GetMapping("/profile")
	public ResponseEntity<ResponseObject> getShopProfileByID() {
		return service.getShopProfileByID();
	}

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/ban/{id}")
	public ResponseEntity<ResponseObject> banShop(@PathVariable int id, @RequestParam String status) {
		return service.banShop(id, status);
	}

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllShops(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int amount) {
		return service.getAllShop(page, amount);
	}

	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createShop(@RequestBody ShopRequest shop) {
		return service.createShop(shop);
	}

	@PreAuthorize("hasAuthority('shop')")
	@PutMapping("/profile/update/{id}")
	public ResponseEntity<ResponseObject> updateShop(@PathVariable int id, @RequestBody ShopRequest shop) {
		return service.updateShop(id, shop);
	}

	@GetMapping("/popular")
	public ResponseEntity<ResponseObject> getTop10ShopsByTransactions() {
		return service.getTop10ShopsByTransactions();
	}

	@GetMapping("/get-by-product")
	public ResponseEntity<ResponseObject> getShopByProductId(@RequestParam int productId) {
		return service.getShopByProductId(productId);
	}

}
