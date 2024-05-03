package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1")
public class ShopController {
	@Autowired
	private ShopService service;

	@GetMapping("/shop/profile/{id}")
	public ResponseEntity<ResponseObject> getShopByID(@PathVariable int id) {
		return service.getShopByID(id);
	}
	
    @GetMapping("/shop/get-all")
    public ResponseEntity<ResponseObject> getAllShops(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int amount
    ) {
        return service.getAllShop(page, amount);
    }
//
//	@GetMapping("/shop/account/{id}")
//	public ResponseEntity<ResponseObject> getShopByAccountID(HttpServletRequest request, @PathVariable int id) {
//		return service.getShopByAccountID(request, id);
//	}
//
	@PostMapping("/shop/create")
	public ResponseEntity<ResponseObject> createShop(@RequestBody ShopRequest shop) {
		return service.createShop(shop);
	}

	@PutMapping("/shop/profile/update/{id}")
	public ResponseEntity<ResponseObject> updateShop(@PathVariable int id,
			@RequestBody ShopRequest shop) {
		return service.updateShop(id, shop);
	}
	
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
