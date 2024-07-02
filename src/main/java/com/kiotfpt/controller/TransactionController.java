package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.TransactionService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/transaction")
public class TransactionController {

	@Autowired
	private TransactionService service;

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllTransactionByAccountID() {
		return service.getTransactionByAccountID();
	}
}
