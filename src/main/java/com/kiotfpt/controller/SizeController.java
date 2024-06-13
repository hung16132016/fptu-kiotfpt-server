package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.SizeService;

@RestController
@RequestMapping("/v1/size")
public class SizeController {

	@Autowired
	private SizeService service;

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllSizes() {
		return service.getAllSize();
	}
}
