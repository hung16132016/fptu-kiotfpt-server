package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.kiotfpt.request.CategoryRequest;
import com.kiotfpt.response.GetAllCategoryResponse;
import com.kiotfpt.response.StatusResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ShopCateRepository shopCategoryRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllCategory() {
		List<Category> categories = repository.findAll();
		List<GetAllCategoryResponse> list = categories.stream()
				.map(category -> new GetAllCategoryResponse(category.getId(), category.getName(),
						category.getThumbnail(), new StatusResponse(category.getStatus()),
						category.getProducts().size()))
				.collect(Collectors.toList());
		if (categories.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No categories found", ""));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Categories fetched successfully", list));
	}

	public ResponseEntity<ResponseObject> getPopularCategory() {
		List<Object[]> popularCate = productRepository.findTop4PopularCategory();
		List<Category> categories = new ArrayList<>();

		for (Object[] obj : popularCate) {
			Optional<Category> category = repository.findById(Integer.parseInt(obj[0].toString()));
			categories.add(category.get());
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("categoryFound"), categories));
	}

	public ResponseEntity<ResponseObject> getCategoryById(int id) {
		Optional<Category> category = repository.findById(id);

		if (category.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Category Found", category.get()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Category not found", null));
		}
	}

	public ResponseEntity<ResponseObject> createCategory(CategoryRequest request) {
		Optional<Category> foundCategory = repository.findByName(request.getName().trim());
		if (!foundCategory.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Category existed");

		Category newCategory = null;
		if (tokenUtils.checkMatch("shop") == true) {
			newCategory = new Category(request, statusRepository.findByValue("inactive").get());
		} else if (tokenUtils.checkMatch("admin") == true)
			newCategory = new Category(request, statusRepository.findByValue("active").get());

		repository.save(newCategory);

		if (tokenUtils.checkMatch("shop") == true) {
			// Create new ShopCategory entry
			Optional<Shop> foundShop = shopRepository.findByAccount(tokenUtils.getAccount());
			if (foundShop.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
						"Shop not found with account id " + tokenUtils.getAccount().getId());
			}

			ShopCategory newShopCategory = new ShopCategory();
			newShopCategory.setCategory(newCategory);
			newShopCategory.setShop(foundShop.get());
			newShopCategory.setStatus(statusRepository.findByValue("active").get());
			shopCategoryRepository.save(newShopCategory);
		}

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, responseMessage.get("createCategorySuccess"),
				newCategory);
	}

	public ResponseEntity<ResponseObject> updateCategory(int id, CategoryRequest request) {
		Optional<Category> optionalCategory = repository.findById(id);
		if (!optionalCategory.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Category with id: " + id + " not found", null));
		}

		Category category = optionalCategory.get();
		category.setName(request.getName());
		category.setThumbnail(request.getThumbnail());

		repository.save(category);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Category updated successfully", null));
	}

	public ResponseEntity<ResponseObject> deleteCategory(int id) {
		Optional<Category> optionalCategory = repository.findById(id);
		if (!optionalCategory.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Category with id: " + id + " not found", null));
		}

		Category category = optionalCategory.get();
		Optional<Status> deleteStatus = statusRepository.findById(12);

		category.setStatus(deleteStatus.get());
		repository.save(category);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Category deleted successfully", null));
	}

	public ResponseEntity<ResponseObject> getCategoriesByShop(int shopId) {
		List<Category> categories = shopCategoryRepository.findCategoriesByShopId(shopId);

		List<Category> activeCategories = categories.stream()
				.filter(category -> "active".equalsIgnoreCase(category.getStatus().getValue()))
				.collect(Collectors.toList());

		if (categories.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No categories found for the shop", null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Categories fetched successfully", activeCategories));

	}

	public ResponseEntity<ResponseObject> changeCategoryStatus(int categoryId, String stat) {
		Optional<Category> optionalCategory = repository.findById(categoryId);
		if (!optionalCategory.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Category with id: " + categoryId + " not found", null));
		}

		Optional<Status> optionalStatus = statusRepository.findByValue(stat);
		if (!optionalStatus.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status with id: " + stat + " not found", null));
		}

		Category category = optionalCategory.get();
		category.setStatus(optionalStatus.get());

		repository.save(category);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Category status updated successfully", category));
	}

}
