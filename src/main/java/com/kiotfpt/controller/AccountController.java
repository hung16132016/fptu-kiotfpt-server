package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.AccountService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1")
public class AccountController {
	@Autowired
	private AccountService service;

	@GetMapping("/account/get-all")
	public ResponseEntity<ResponseObject> getAllAccount() {
		return service.getAllAccount();
	}

	
//
//	@PutMapping("/profile/update/{id}")
//	public ResponseEntity<ResponseObject> updateProfile(@PathVariable int id, @RequestBody AccountProfile profile,
//			HttpServletRequest request) {
//		return service.updateProfile(id, profile, request);
//	}
//
//	@PostMapping("/account/update-wallet")
//	public ResponseEntity<ResponseObject> updateWallet(@RequestBody Map<String, String> obj)
//			throws AddressException, MessagingException {
//		return service.updateWallet(obj);
//	}
//
//	@PutMapping("/account/check-otp-add-money-to-wallet")
//	public ResponseEntity<ResponseObject> checkOtpWallet(@RequestBody Map<String, String> obj) {
//		return service.checkOtpWallet(obj);
//	}
//
//	@PutMapping("/account/check-otp-payment")
//	public ResponseEntity<ResponseObject> checkOtpPayment(@RequestBody Map<String, String> obj) {
//		return service.checkOtpPayment(obj);
//	}
}
