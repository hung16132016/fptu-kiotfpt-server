package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.VoucherService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/v1/voucher")
public class VoucherController {

	@Autowired
	private VoucherService service;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getVoucherByShopID(@PathVariable int id) {
		return service.getVoucherByShopID(id);
	}
}
