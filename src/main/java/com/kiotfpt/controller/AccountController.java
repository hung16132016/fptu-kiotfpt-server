package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.AccountService;

@RestController
@RequestMapping("/v1")
public class AccountController {
	@Autowired
	private AccountService service;

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/account/get-all")
	public ResponseEntity<ResponseObject> getAllAccount() {
		return service.getAllAccount();
	}

}
