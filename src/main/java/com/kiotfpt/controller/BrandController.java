package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.BrandService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/brand")
@Service
public class BrandController {

	@Autowired
	private BrandService service;
	
	@GetMapping("/get-by-category")
	public ResponseEntity<ResponseObject> getBrandByCategoryID(@RequestParam(name = "categoryID") int categoryID) {
		return service.getBrandByCategoryID(categoryID);
	}
}
