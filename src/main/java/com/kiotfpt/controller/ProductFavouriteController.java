package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.ProductFavouriteService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/favourite")
public class ProductFavouriteController {
	@Autowired
	private ProductFavouriteService service;

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllProductFavouriteByAccountID(@RequestParam(name = "accountID") int id) {
		return service.getAllProductFavouriteByAccountID(id);
	}
	
	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteProductFavouriteById(@PathVariable int id) {
		return service.deleteProductFavouriteById(id);
    }
	
	
	@PreAuthorize("hasAuthority('user')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createProductFavourite(@RequestParam(name = "productID") int id) {
        return service.createProductFavourite(id);
    }
}
