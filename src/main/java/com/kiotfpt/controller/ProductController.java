package com.kiotfpt.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.Product;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.ProductService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/product/get-all")
	public ResponseEntity<ResponseObject> getAllProduct() {
		return service.getAllProduct();
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<ResponseObject> getProductById(@PathVariable int id) {
		return service.getProductById(id);
	}
	
	@GetMapping("/product/get-by")
	public ResponseEntity<ResponseObject> getProduct(
			@RequestParam(name = "shop_id", required = false) Integer shopID,
			@RequestParam(name = "category_id", required = false) Integer categoryID) {
		if (shopID != null && categoryID != null) {
			// Handle the case when both parameters are provided
			// You can return an appropriate response or throw an exception.
			// For example:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							"Only one of shop-id or account-id should be provided", ""));
		} else if (shopID != null) {
			return service.findByShopId(shopID);
		} else if (categoryID != null) {
			return service.findByCategoryId(categoryID);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Invalid request parameters", ""));
		}
	}

	@PostMapping("/product/create")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody Map<String, String> obj) throws IOException{
		return service.createProduct(obj);
	}

	@DeleteMapping("product/delete/{id}")
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable int id) {
		return service.deleteProduct(id);
	}

	@PutMapping("/product/update/{id}")
	public ResponseEntity<ResponseObject> createProduct(@PathVariable int id,
			@RequestBody Map<String, String> obj) {
		return service.updateProduct(id, obj.get("name"), obj.get("description"), obj.get("price"),
				Integer.parseInt(obj.get("category_id")));
	}
	
	@GetMapping("/product/search")
	public List<Product> searchByName(@RequestParam(name = "query", required = true) String name) {
		return service.searchByName(name);
	}

//	@GetMapping("/product/get-list-popular")
//	public ResponseEntity<ResponseObject> getProductPopular() {
//		return service.getProductPopular();
//	}
//	
//	@GetMapping("/product/get-list-product")
//	public ResponseEntity<ResponseObject> getProductByPage(@RequestParam("page") int page) {
//		return service.getProductByPage(page);
//	}
//	
//	@GetMapping("/product")
//	public ResponseEntity<ResponseObject> getProductByID(@RequestParam("id") int product_id) {
//		return service.getProductByID(product_id);
//	}
//	
//	@GetMapping("/product/filter")
//	public ResponseEntity<ResponseObject> getProductByID(@RequestParam("price") String price) {
//		return service.getSortedProducts(price);
//	}
//	
//	@GetMapping("/product/search")
//	public ResponseEntity<ResponseObject> getProductByName(@RequestParam("name") String name) {
//		return service.getProductByName(name);
//	}

}
