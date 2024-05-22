package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductFavourite;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.ProductFavouriteRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.request.ProductFavouriteRequest;
import com.kiotfpt.response.ProductResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class ProductFavouriteService {

	@Autowired
	private ProductFavouriteRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ProductRepository productRepository;
	
	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllProductFavouriteByAccountID(int id) {
		Optional<Account> acc = accountRepository.findById(id);

		if (!acc.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), null));
		}

		List<ProductFavourite> productFavourites = repository.findByAccount(acc.get());

		if (productFavourites.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "No favourtie product found", productFavourites));
		}

		List<ProductResponse> productResponses = productFavourites.stream()
				.map(productFavourite -> new ProductResponse(productFavourite.getProduct()))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Favourite products found", productResponses));
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

	public ResponseEntity<ResponseObject> createProductFavourite(ProductFavouriteRequest request) {
		// Fetch the Account
		Account account = accountRepository.findById(request.getAccount_id()).orElse(null);
		if (account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Account with id: " + request.getAccount_id() + " not found", null));
		}

		// Fetch the Product
		Product product = productRepository.findById(request.getProduct_id()).orElse(null);
		if (product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Product with id: " + request.getProduct_id() + " not found", null));
		}

		// Create a new ProductFavourite entity
		ProductFavourite productFavourite = new ProductFavourite();
		productFavourite.setAccount(account);
		productFavourite.setProduct(product);

		// Save the ProductFavourite entity
		repository.save(productFavourite);

		// Return a successful response
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product added to favourites successfully", productFavourite));
	}

}
