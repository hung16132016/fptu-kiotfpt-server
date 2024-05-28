package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.ShopCateService;

@RestController
@RequestMapping("/api/shopcate")
public class ShopCateController {

    @Autowired
    private ShopCateService service;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addShopCategory(@RequestParam int shopID, @RequestParam int categoryID) {
        return service.addShopCategory(shopID, categoryID);
    }
    
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseObject> removeShopCategoryById(@PathVariable int id) {
        return service.removeShopCategoryById(id);
    }
    
    @GetMapping("/get-by-shop")
    public ResponseEntity<ResponseObject> getShopCategoryByShopID(@RequestParam int shopID) {
        return service.getShopCategoryByShopID(shopID);
    }
}
