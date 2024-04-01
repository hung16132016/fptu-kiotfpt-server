package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.Category;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.CategoryService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/category")
@Service
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllCategory() {
		return service.getAllCategory();
	}

//	@PostMapping("/category/add")
//	public ResponseEntity<ResponseObject> createCategory(@RequestBody Category newCategory) {
//		return service.createCategory(newCategory);
//
//	}
//
//	@PutMapping("/category/{id}")
//	public ResponseEntity<ResponseObject> updateCategory(@PathVariable int id, @RequestBody Category category) {
//		return service.updateCategory(id, category);
//	}
}
