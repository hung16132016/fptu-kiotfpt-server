package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.kiotfpt.request.BrandRequest;
import com.kiotfpt.service.BrandService;
import com.kiotfpt.utils.ResponseObjectHelper;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/brand")
public class BrandController {

	@Autowired
	private BrandService service;

	@GetMapping("/get-by-category")
	public ResponseEntity<ResponseObject> getBrandByCategoryID(@RequestParam(name = "categoryID") int categoryID) {
		return service.getBrandByCategoryID(categoryID);
	}

	@GetMapping("/popular")
	public ResponseEntity<ResponseObject> getPopularBrands() {
		return service.getPopularBrands();
	}

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllBrands() {
		return service.getAllBrands();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getBrandById(@PathVariable int id) {
		return service.getBrandById(id);

	}

	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createBrand(@RequestBody BrandRequest brandRequest) {
		return service.createBrand(brandRequest);
	}

	@GetMapping("/update-status/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ResponseObject> updateStatusBrand(@PathVariable int id, @RequestParam String status) {
		if (status.equalsIgnoreCase("inactive"))
			return service.deleteBrand(id);
		else if (status.equalsIgnoreCase("active"))
			return service.activateBrand(id);
		else
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Invalid status");
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ResponseObject> updateBrand(@PathVariable int id, @RequestBody BrandRequest brandRequest) {
		return service.updateBrand(id, brandRequest);
	}

}
