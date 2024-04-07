package com.kiotfpt.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.OrderService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/api/v1/transaction")
public class OrderController {

	@Autowired
	private OrderService service;

	@GetMapping("/get-by")
	public ResponseEntity<ResponseObject> getOrder(
			@RequestParam(name = "shop-id", required = false) Integer shopID,
			@RequestParam(name = "account-id", required = false) Integer accountID) {
		if (shopID != null && accountID != null) {
			// Handle the case when both parameters are provided
			// You can return an appropriate response or throw an exception.
			// For example:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							"Only one of shop-id or account-id should be provided", ""));
		} else if (shopID != null) {
			return service.getOrderByShopID(shopID);
		} else if (accountID != null) {
			return service.getOrderByAccountID(accountID);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Invalid request parameters", ""));
		}
	}

//	@PutMapping("/update")
//	public ResponseEntity<ResponseObject> updateOrder(@RequestBody Order transaction) {
//		return service.updateOrder(transaction);
//	}

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
