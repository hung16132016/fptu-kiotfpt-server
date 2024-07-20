package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.kiotfpt.service.ShopCateService;
import com.kiotfpt.utils.ResponseObjectHelper;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/shopcate")
public class ShopCateController {

    @Autowired
    private ShopCateService service;

    @PreAuthorize("hasAuthority('shop')")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addShopCategory(@RequestParam int shopID, @RequestParam int categoryID) {
        return service.addShopCategory(shopID, categoryID);
    }
    
    @PreAuthorize("hasAuthority('shop')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseObject> removeShopCategoryById(@PathVariable int id) {
        return service.removeShopCategoryById(id);
    }
    
    @PreAuthorize("hasAuthority('shop')")
    @GetMapping("/get-by-shop")
    public ResponseEntity<ResponseObject> getShopCategoryByShopID() {
        return service.getShopCategoryByShopID();
    }
    
	@GetMapping("/update-status/{id}")
	@PreAuthorize("hasAuthority('shop')")
	public ResponseEntity<ResponseObject> updateStatusBrand(@PathVariable int id, @RequestParam String status) {
		if (status.equalsIgnoreCase("inactive"))
			return service.deleteShopCate(id);
		else if (status.equalsIgnoreCase("active"))
			return service.activateShopCate(id);
		else
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Invalid status");
	}
}
