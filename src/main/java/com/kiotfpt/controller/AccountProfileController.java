package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.AccountProfileService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1")
public class AccountProfileController {
	@Autowired
	private AccountProfileService service;

	@GetMapping("/profile/{id}")
	public ResponseEntity<ResponseObject> getProfileByAccountID(@PathVariable int id) {
		return service.getProfileByAccountID(id);
	}
}
