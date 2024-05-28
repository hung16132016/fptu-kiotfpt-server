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
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.CategoryRequest;
import com.kiotfpt.utils.JsonReader;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StatusRepository statusRepository;

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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Category existed", ""));

		Category newCategory = new Category(request, statusRepository.findByValue("active").get());
		repository.save(newCategory);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("createCategorySuccess"), newCategory));
	}

    public ResponseEntity<ResponseObject> updateCategory(int id, CategoryRequest request) {
        Optional<Category> optionalCategory = repository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "Category with id: " + id + " not found", null));
        }

        Category category = optionalCategory.get();
        category.setName(request.getName());
        category.setThumbnail(request.getThumbnail());
        // Assuming status remains the same and not updated via request
        // If status needs to be updated, fetch the status from statusRepository and set it.

        repository.save(category);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
                        "Category updated successfully", null));
    }
    
    public ResponseEntity<ResponseObject> deleteCategory(int id) {
    	Optional<Category> optionalCategory = repository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "Category with id: " + id + " not found", null));
        }
        
        Category category = optionalCategory.get();
        Optional<Status> deleteStatus = statusRepository.findById(12);
        
        category.setStatus(deleteStatus.get());
        repository.save(category);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
                        "Category deleted successfully", null));
    }

}
