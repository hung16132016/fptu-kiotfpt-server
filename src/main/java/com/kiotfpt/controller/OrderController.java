package com.kiotfpt.controller;

import java.util.Map;

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
import com.kiotfpt.service.OrderService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/v1/order")
public class OrderController {

	@Autowired
	private OrderService service;

//	@GetMapping("/get-by")
//	public ResponseEntity<ResponseObject> getOrder(
//			@RequestParam(name = "shop-id", required = false) Integer shopID,
//			@RequestParam(name = "account-id", required = false) Integer accountID) {
//		if (shopID != null && accountID != null) {
//			// Handle the case when both parameters are provided
//			// You can return an appropriate response or throw an exception.
//			// For example:
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//							"Only one of shop-id or account-id should be provided", ""));
//		} else if (shopID != null) {
//			return service.getOrderByShopID(shopID);
//		} else if (accountID != null) {
//			return service.getOrderByAccountID(accountID);
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Invalid request parameters", ""));
//		}
//	}

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllOrderByAccountID(@RequestParam(name = "accountID") int id) {
		return service.getOrderByAccountID(id);
	}
	
	@GetMapping("/get-by-shop")
	public ResponseEntity<ResponseObject> getProductByShop(@RequestParam(name = "shop_id") Integer shopID,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int amount) {
		return service.getOrderByShopID(shopID, page, amount);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseObject> updateOrder(@PathVariable int id, @RequestBody String status_value) {
		return service.updateOrderStatus(id, status_value);
	}

	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createOrder(@RequestBody Map<String, String> map) {
		return service.createOrder(map);
	}

	@GetMapping("/get-current")
	public ResponseEntity<ResponseObject> getCurrentOrders(
			@RequestParam(name = "account_id", required = true) Integer id) {
		return service.getCurrentOrders(id);
	}

//	@PostMapping("/payment/check")
//	public ResponseEntity<ResponseObject> checkOtpPayment(@RequestBody Map<String, String> map,
//			HttpServletRequest request) {
//		return service.checkOtpPayment(map, request);
//	}
}
