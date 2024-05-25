package com.kiotfpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Category;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.ShopCategory;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ShopCateRepository;
import com.kiotfpt.repository.ShopRepository;

@Service
public class ShopCateService {

    @Autowired
    private ShopCateRepository shopCategoryRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<ResponseObject> addShopCategory(int shopID, int categoryID) {
        if (!shopRepository.existsById(shopID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "Shop with id: " + shopID + " not found", null));
        }

        if (!categoryRepository.existsById(categoryID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "Category with id: " + categoryID + " not found", null));
        }

        Shop shop = shopRepository.findById(shopID).get();
        Category category = categoryRepository.findById(categoryID).get();

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShop(shop);
        shopCategory.setCategory(category);

        shopCategoryRepository.save(shopCategory);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
                        "Shop category added successfully", null));
    }
    
    public ResponseEntity<ResponseObject> removeShopCategoryById(int id) {
        if (!shopCategoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "ShopCategory with id: " + id + " not found", null));
        }

        shopCategoryRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
                        "ShopCategory removed successfully", null));
    }
}
