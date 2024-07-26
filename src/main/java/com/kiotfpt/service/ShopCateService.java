package com.kiotfpt.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Category;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.ShopCategory;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ShopCateRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class ShopCateService {

	@Autowired
	private ShopCateRepository repository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TokenUtils tokenUtils;

	public ResponseEntity<ResponseObject> addShopCategory(int shopID, int categoryID) {
		if (!shopRepository.existsById(shopID)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop with id: " + shopID + " not found", null));
		}

		if (!categoryRepository.existsById(categoryID)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Category with id: " + categoryID + " not found", null));
		}

		Shop shop = shopRepository.findById(shopID).get();
		Category category = categoryRepository.findById(categoryID).get();

		// Check if the shop already has the category
	    boolean categoryExists = repository.existsByShopAndCategory(shop, category);
	    if (categoryExists) {
	        return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
	                "The shop already has this category");
	    }
	    
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShop(shop);
		shopCategory.setCategory(category);
		shopCategory.setStatus(statusRepository.findByValue("active").get());

		repository.save(shopCategory);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Shop category added successfully", null));
	}

	public ResponseEntity<ResponseObject> removeShopCategoryById(int id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "ShopCategory with id: " + id + " not found", null));
		}

		repository.deleteById(id);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "ShopCategory removed successfully", null));
	}

	public ResponseEntity<ResponseObject> getShopCategoryByShopID() {
		Optional<Shop> optShop = shopRepository.findByAccount(tokenUtils.getAccount());

		if (optShop.isEmpty()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					"No shop found for the current account");
		}

		Shop shop = optShop.get();
		Collection<ShopCategory> shopCategories = shop.getShopcategories();

		if (shopCategories.isEmpty()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "This shop has no shop categories");
		}

		Collection<ShopCategoryResponse> listShopCate = shopCategories.stream().map(shopCate -> {
			int totalProducts = productRepository
					.findByShopIDAndCateID(shopCate.getShop().getId(), shopCate.getCategory().getId()).size();
			return new ShopCategoryResponse(totalProducts, shopCate);
		}).collect(Collectors.toList());

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Get shop category by shop ID successfully",
				listShopCate);
	}

	public ResponseEntity<ResponseObject> deleteShopCate(int id) {

		ShopCategory shopCate = repository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
		Status inactiveStatus = statusRepository.findByValue("inactive")
				.orElseThrow(() -> new RuntimeException("Inactive status not found"));
		shopCate.setStatus(inactiveStatus);
		repository.save(shopCate);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Brand deleted successfully", null));
	}

	public ResponseEntity<ResponseObject> activateShopCate(int id) {
		Optional<ShopCategory> optionalShopCate = repository.findByIdAndStatusValue(id, "inactive");

		if (optionalShopCate.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Brand not found or already active", null));
		}

		ShopCategory shopCate = optionalShopCate.get();
		Optional<Status> activeStatus = statusRepository.findByValue("active");

		if (activeStatus.isEmpty()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(false,
					HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0], "Active status not found", null));
		}

		shopCate.setStatus(activeStatus.get());
		repository.save(shopCate);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Brand activated successfully", null));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class ShopCategoryResponse {
		private int totalProducts;
		private ShopCategory shopCate;
	}
}
