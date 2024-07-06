package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductFavourite;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.ProductFavouriteRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

@Service
public class ProductFavouriteService {

	@Autowired
	private ProductFavouriteRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllProductFavouriteByAccountID() {
		Account acc = tokenUtils.getAccount();

		List<ProductFavourite> productFavourites = repository.findByAccount(acc);

		if (productFavourites.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(true,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No favourtie product found", productFavourites));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Favourite products found", productFavourites));
	}

	public ResponseEntity<ResponseObject> deleteProductFavouriteById(int id) {

		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Product favourite with id: " + id + " not found", null));
		}

		repository.deleteById(id);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Delete favourite product successfull", null));
	}

	public ResponseEntity<ResponseObject> createProductFavourite(int id) {
		// Fetch the Account
		Account account = tokenUtils.getAccount();

		// Fetch the Product
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					"Product with id: " + id + " not found");
		}

		ProductFavourite productFavourite = new ProductFavourite();
		productFavourite.setAccount(account);
		productFavourite.setProduct(product);

		repository.save(productFavourite);

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Product added to favourites successfully",
				productFavourite);
	}

}
