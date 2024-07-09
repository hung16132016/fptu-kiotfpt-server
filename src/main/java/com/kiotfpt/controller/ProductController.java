package com.kiotfpt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllProduct(@RequestParam(name = "page") Integer page,
			@RequestParam(name = "amount") Integer amount) {
		return service.getAllProduct(page, amount);
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<ResponseObject> getProductById(@PathVariable int id) {
		return service.getProductById(id);
	}

	@GetMapping("/new")
	public ResponseEntity<ResponseObject> getNew8Added() {
		return service.getNew8Added();
	}

	@GetMapping("/get-by-category")
	public ResponseEntity<ResponseObject> getByCategoryID(@RequestParam(name = "categoryID") Integer category_id,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getByCategoryID(category_id, page, amount);
	}

	@GetMapping("/get-by-brand")
	public ResponseEntity<ResponseObject> getByBrandID(@RequestParam(name = "brandID") Integer brand_id,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getByBrandID(brand_id, page, amount);
	}

	@GetMapping("/get-by-rate")
	public ResponseEntity<ResponseObject> getByRate(@RequestParam(name = "rate") Float rate,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getProductsByRate(rate, page, amount);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseObject> getByKeyword(@RequestParam(name = "key") String keyword,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getByKeyword(keyword, page, amount);
	}

	@PreAuthorize("hasAuthority('shop')")
	@GetMapping("/get-by-shop")
	public ResponseEntity<ResponseObject> getProductByShop(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int amount) {
		return service.findByShopId(page, amount);
	}

	@PreAuthorize("hasAuthority('shop')")
	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductRequest product) {
		return service.createProduct(product);
	}

	@PreAuthorize("hasAuthority('shop')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable int id, @RequestBody List<Integer> variantIds) {
		return service.deleteProduct(id, variantIds);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseObject> updateProduct(@PathVariable int id, @RequestBody ProductRequest obj) {
		return service.updateProduct(id, obj);
	}

	@GetMapping("/get-by-price-range")
	public ResponseEntity<ResponseObject> getByPriceRange(@RequestParam(name = "min") float min,
			@RequestParam(name = "max") float max, @RequestParam(name = "page") Integer page,
			@RequestParam(name = "amount") Integer amount) {

		return service.getByPriceRange(min, max, page, amount);
	}

	@PostMapping("/get-multiple-category")
	public ResponseEntity<ResponseObject> getByListCategoryID(@RequestBody Map<String, Object> request) {
		List<String> category_id_list = (List<String>) request.get("category_id_list");
		return service.getByListCategoryID(category_id_list);
	}

	@GetMapping("/total-page")
	public ResponseEntity<ResponseObject> getTotalPage(@RequestParam(name = "amount") int amount) {
		return service.getTotalPage();
	}

	@GetMapping("/products-no-comments")
	public ResponseEntity<ResponseObject> getProductsWithoutComments(@RequestParam(name = "accountId") int accountId) {
		return service.getProductsNotCommentedByAccount(accountId);
	}

	@GetMapping("/get-by-shop-and-cat")
	public ResponseEntity<ResponseObject> getProductsByShopCatID(@RequestParam(name = "shopCatID") int shopCatID,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getProductsByShopCatID(shopCatID, page, amount);
	}

	@GetMapping("/get-by-type")
	public ResponseEntity<ResponseObject> getProductsByType(@RequestParam(name = "type") String type,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "amount") Integer amount) {
		return service.getProductsByType(type, page, amount);
	}

	@GetMapping("/get-by-type-and-shop")
	public ResponseEntity<ResponseObject> getProductsByTypeAndShopID(@RequestParam(name = "shopID") Integer shopId,
			@RequestParam(name = "type") String type, @RequestParam(name = "page") Integer page,
			@RequestParam(name = "amount") Integer amount) {
		return service.getProductsByTypeAndShopID(type, shopId, page, amount);
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/bought")
	public ResponseEntity<ResponseObject> getProductsBoughtByAccount() {
		return service.getProductsBoughtByAccount();
	}

}
