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

}
