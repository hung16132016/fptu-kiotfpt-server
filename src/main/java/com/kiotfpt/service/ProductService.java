package com.kiotfpt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.Product_Thumbnail;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ShopRepository shopRepository;

//	@Value("${path_image}")
//	private String imageLink;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllProduct(int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
        Page<Product> productPage = repository.findByStatusId11(pageable);
        
        List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getProductById(int id) {
		Optional<Product> product = repository.findById(id);
		if (product.isPresent() && product.get().getStatus().getValue().equals("active")) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getProducyByIdSuccess"), product));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(true,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("getProducyByIdFail"), ""));

	}
	
	public ResponseEntity<ResponseObject> getNew8Added() {
		Pageable pageable = PageRequest.of(0, 8);
        Page<Product> productPage = repository.findLast8ProductsByStatus11(pageable);
        
        List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	
	public ResponseEntity<ResponseObject> getByCategoryID(int category_id, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
        Page<Product> productPage = repository.findByStatus11AndCategoryId(pageable, category_id);
        
        List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	
	public ResponseEntity<ResponseObject> getByKeyword(String keyword, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
        Page<Product> productPage = repository.findByStatus11AndKeyword(pageable, keyword);
        
        List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	

	public ResponseEntity<ResponseObject> updateProduct(int product_id, String name, String description, String price,
			int category_id) {
		Optional<Product> pro = repository.findById(product_id);

		if (!pro.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("productNotFound"), new int[0]));
		}
//		check empty
		if (name == "" || description == "" || price == "") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input can not be empty!", new int[0]));
		}
		if (name.length() >= 255 || description.length() >= 511 || price.length() >= 12)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input too long", new int[0]));
//		chheck category
		Optional<Category> category = categoryRepository.findById(category_id);
		if (category.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "category not exist!", new int[0]));
		}

		Product product = pro.get();
		product.setProduct_name(name);
		product.setProduct_description(description);
		product.setProduct_price(Float.parseFloat(price));
		product.setCategory(category.get());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "product is updated successfully", repository.save(product)));
	}

	public ResponseEntity<ResponseObject> deleteProduct(int id) {
		Optional<Product> pro = repository.findById(id);

		if (!pro.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "product not exist", new int[0]));
		}
		Optional<Status> statusDeleted = statusRepository.findByValue("Deleted");
		Product product = pro.get();
		product.setStatus(statusDeleted.get());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product is deleted successfully", repository.save(product)));
	}

	public ResponseEntity<ResponseObject> createProduct(Map<String, String> obj) throws IOException {

		String name = obj.get("name"), description = obj.get("description"), price = obj.get("price"),
				category_id = obj.get("category_id"), variants = obj.get("variants"), shop_id = obj.get("shop_id");
//		check empty
		if (name == "" || description == "" || price == "" || category_id == "" || variants == "") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input can not be empty!", new int[0]));
		}
		if (name.length() >= 255 || description.length() >= 511 || price.length() >= 12)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input too long", new int[0]));
//		chheck category
		Optional<Category> category = categoryRepository.findById(Integer.parseInt(category_id));
		if (!category.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "category not exist!", new int[0]));
		}
//		chheck shop
		Optional<Shop> shop = shopRepository.findById(Integer.parseInt(shop_id));
		if (!shop.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "category not exist!", new int[0]));
		}
		Product product = new Product();

		// Get the thumbnail JSON string
		String thumbnailJsonString = obj.get("thumbnail");

		// Create ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		// Parse the thumbnail JSON string into a JsonNode
		JsonNode thumbnailJson = objectMapper.readTree(thumbnailJsonString);

		// Create a list to store thumbnail objects
		List<Product_Thumbnail> thumbnails = new ArrayList<>();

		// Iterate over the elements in the thumbnail JSON object
		for (JsonNode entry : thumbnailJson) {
			String link = entry.get("link").asText();

			// Assuming Product_thumbnail has a constructor that takes a link
			Product_Thumbnail thumbnail = new Product_Thumbnail(link);

			// Set the product for each thumbnail
			thumbnail.setProduct(product);

			thumbnails.add(thumbnail);
		}

		product.setProduct_name(name);
		product.setProduct_description(description);
		product.setProduct_price(Float.parseFloat(price));
		product.setProduct_popular(false);
		product.setProduct_best_seller(false);
		product.setStatus(statusRepository.findById(1).get()); // active
		product.setCategory(category.get());
		product.setProduct_variants(variants);
		product.setThumbnail(thumbnails);
		product.setShop(shop.get());
		repository.save(product);

		product = repository.findAll().get(repository.findAll().size() - 1);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product is created successfull", product));
	}



	public ResponseEntity<ResponseObject> findByShopId(int id, int page, int amount) {
	    Optional<Shop> shopOptional = shopRepository.findById(id);
	    if (shopOptional.isPresent()) {
	        Pageable pageable = PageRequest.of(page - 1, amount);
	        Page<Product> productPage = repository.findAllByShopId(id, pageable);
	        
	        if (!productPage.isEmpty()) {
	            List<Product> products = productPage.getContent();
	            List<Product> returnListProduct = new ArrayList<>();
	            
	            for (Product product : products) {
	                int status = product.getStatus().getStatus_id();
	                if (status != 2 && status != 3 && status != 4) {
	                    returnListProduct.add(product);
	                }
	            }
	            
	            if (!returnListProduct.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.OK)
	                        .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
	                                responseMessage.get("getProductByShopIdSuccess"), returnListProduct));
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
	                                responseMessage.get("getProductByShopIdFail"), ""));
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
	                            responseMessage.get("getProductByShopIdFail"), ""));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
	                        responseMessage.get("notFindShop"), ""));
	    }
	}


	public ResponseEntity<ResponseObject> findByCategoryId(@PathVariable int id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent()) {
			List<Product> foundProduct = repository.findAllByCategoryid(id);
			if (foundProduct.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								responseMessage.get("getProductByShopIdFail"), ""));
			} else {
				List<Product> returnListProduct = new ArrayList<>(); // List to store products with status not 2, 3, or
																		// 4
				// Iterate through foundProduct list to check status
				for (Product product : foundProduct) {
					int status = product.getStatus().getStatus_id();
					// Check if status is not 2, 3, or 4
					if (status != 2 && status != 3 && status != 4) {
						returnListProduct.add(product);
					}
				}
				if (!returnListProduct.isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdSuccess"), returnListProduct));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdFail"), ""));
				}
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("notFindCate"), ""));
		}

	}
	
	public List<Product> searchByName(@PathVariable(name = "name") String name) {
		List<Product> found = repository.findByname(name);
		return !found.isEmpty() ? repository.findByname(name) : null;
	}

//	public ResponseEntity<ResponseObject> getProductPopular() {
//		List<Product> products = repository.findByProductPopularIsTrue();
//		
//		// Filter out products with status 4
//        List<Product> filteredProducts = products.stream()
//                .filter(product -> product.getStatus().getStatus_id() != 4)
//                .collect(Collectors.toList());
//		
//		return !filteredProducts.isEmpty()
//				? ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//											"Data has found successfully", filteredProducts))
//				: ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
//	}
//	
//	public ResponseEntity<ResponseObject> getProductByPage(int page) {
//		int pageSize = 10;
//		if (page <= 0)
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0], "page must greater 0", new int[0]));
//		
//		PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//		
//		Page<Product> productPage = repository.findAll(pageRequest);
//
//        // Extract the list of products from the page
//        List<Product> products = productPage.getContent();
//        
//     // Filter out products with status 4
//        List<Product> filteredProducts = products.stream()
//                .filter(product -> product.getStatus().getStatus_id() != 4)
//                .collect(Collectors.toList());
//		
//		return !filteredProducts.isEmpty()
//				? ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//											"Data has found successfully", filteredProducts))
//				: ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
//	
//	public ResponseEntity<ResponseObject> getSortedProducts(String price) {
//		price = price.strip();
//		List<Product> products = repository.findAll();
//		
//		List<Product> filteredProducts = products.stream()
//                .filter(product -> product.getStatus().getStatus_id() != 4)
//                .collect(Collectors.toList());
//		products = filteredProducts;
//		
//		if (price.equals("low-to-high")) {
//			Collections.sort(products, (p1, p2) -> Double.compare(Double.parseDouble(p1.getProduct_price()), Double.parseDouble(p2.getProduct_price())));
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//							"Data has found successfully", products));
//		} else if (price.equals("high-to-low")) {
//			Collections.sort(products, (p1, p2) -> Double.compare(Double.parseDouble(p2.getProduct_price()), Double.parseDouble(p1.getProduct_price())));
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//							"Data has found successfully", products));
//		} if (price == "") {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input can not be empty", new int[0]));
//		}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//				.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0], "wrong input", new int[0]));
//	}
//	
//	public ResponseEntity<ResponseObject> getProductByName(String name) {
//		if(name.strip() == "") {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0], "input can not be empty", new int[0]));
//		}
//		
//		List<Product> products = repository.findByNameContainingIgnoreCase(name);
//		List<Product> filteredProducts = products.stream()
//                .filter(product -> product.getStatus().getStatus_id() != 4)
//                .collect(Collectors.toList());
//		
//		return !filteredProducts.isEmpty()
//				? ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//											"Data has found successfully", filteredProducts))
//				: ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
//	}
//	

}
