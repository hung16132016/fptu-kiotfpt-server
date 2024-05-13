package com.kiotfpt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.ProductRequest;
import com.kiotfpt.service.ProductService;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Autowired
	private ProductService service;

//	api line 13
	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllProduct(@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getAllProduct(page, amount);
	}

//	api line 18
	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getProductById(@PathVariable int id) {
		return service.getProductById(id);
	}
	
//	api line 11
	@GetMapping("/new")
	public ResponseEntity<ResponseObject> getNew8Added() {
		return service.getNew8Added();
	}
	
//	api line 14
	@GetMapping("/get-by-category")
	public ResponseEntity<ResponseObject> getByCategoryID(@RequestParam(name = "categoryID") Integer category_id, @RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getByCategoryID(category_id, page, amount);
	}
	
//	api line 7
	@GetMapping("/search")
	public ResponseEntity<ResponseObject> getByKeyword(@RequestParam(name = "key") String keyword, @RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getByKeyword(keyword, page, amount);
	}

	@GetMapping("/get-by-shop")
	public ResponseEntity<ResponseObject> getProductByShop(@RequestParam(name = "shop_id") Integer shopID,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int amount) {
		return service.findByShopId(shopID, page, amount);
	}

	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductRequest product) throws IOException {
		return service.createProduct(product);
	}

	@DeleteMapping("product/delete/{id}")
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable int id) {
		return service.deleteProduct(id);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseObject> updateProduct(@PathVariable int id, @RequestBody ProductRequest obj) {
		return service.updateProduct(id, obj);
	}
	
	@GetMapping("/discount")
	public ResponseEntity<ResponseObject> getDiscountedProducts() {
		return service.getDiscountedProducts();
	}

	@GetMapping("/official")
	public ResponseEntity<ResponseObject> getOfficialProducts() {
		return service.getOfficialProducts();
	}
	
	@GetMapping("/top-deal")
	public ResponseEntity<ResponseObject> getTopDealProduct() {
		return service.getTopDealProduct();
	}

//	@GetMapping("/search")
//	public List<Product> searchByName(@RequestParam(name = "query", required = true) String name) {
//		return service.searchByName(name);
//	}

//	@GetMapping("/get-list-popular")
//	public ResponseEntity<ResponseObject> getProductPopular() {
//		return service.getProductPopular();
//	}
//	
//	@GetMapping("/get-list-product")
//	public ResponseEntity<ResponseObject> getProductByPage(@RequestParam("page") int page) {
//		return service.getProductByPage(page);
//	}
//	
//	@GetMapping("")
//	public ResponseEntity<ResponseObject> getProductByID(@RequestParam("id") int product_id) {
//		return service.getProductByID(product_id);
//	}
//	
//	@GetMapping("/filter")
//	public ResponseEntity<ResponseObject> getProductByID(@RequestParam("price") String price) {
//		return service.getSortedProducts(price);
//	}
//	
//	@GetMapping("/search")
//	public ResponseEntity<ResponseObject> getProductByName(@RequestParam("name") String name) {
//		return service.getProductByName(name);
//	}

}
