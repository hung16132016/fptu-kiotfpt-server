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
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private ProductRepository productRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllCategory() {
		List<Category> categories = repository.findAll();

		List<Category> filteredCategories = categories.stream()
				.filter(category -> category.getStatus().getValue().equals("active")).collect(Collectors.toList());

		return !filteredCategories.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								responseMessage.get("categoryFound"), filteredCategories))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								responseMessage.get("categoryNotFound"), filteredCategories));
	}
	
	public ResponseEntity<ResponseObject> getPopularCategory() {
		List<Object[]> popularCate = productRepository.findTop4PopularCategory();
		List<Category> categories = new ArrayList<>();
		
		for (Object[] obj : popularCate) {
			Optional<Category> category = repository.findById(Integer.parseInt(obj[0].toString()));
			categories.add(category.get());
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
						responseMessage.get("categoryFound"), categories));
	}

//	public ResponseEntity<ResponseObject> createCategory(Category newCategory) {
//		List<Category> foundProduct = repository.findByName(newCategory.getName().trim());
//		if (!foundProduct.isEmpty())
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("categoryEmpty"), ""));
//
//		if (foundProduct.size() > 0) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("createCategoryFail"), ""));
//		}
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//						responseMessage.get("createCategorySuccess"), repository.save(newCategory)));
//	}
//
//	public ResponseEntity<ResponseObject> updateCategory(int id, Category cate) {
//		Optional<Category> category = repository.findById(id);
//		if (!category.isPresent())
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("categoryEmpty"), ""));
//		if (category.isPresent()) {
//			category.map(p -> {
//				p.setName(cate.getName());
//				p.setStatus(cate.getStatus());
//				return repository.save(p);
//			});
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//							responseMessage.get("updateCategorySuccess"), category.get()));
//		}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//				HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("updateCategoryFail"), ""));
//	}

}
