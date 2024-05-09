package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.Product_Condition;
import com.kiotfpt.model.Product_Thumbnail;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.BrandRepository;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.ProductRequest;
import com.kiotfpt.response.BrandResponse;
import com.kiotfpt.response.CategoryResponse;
import com.kiotfpt.response.ProductResponse;
import com.kiotfpt.response.Product_ConditionResponse;
import com.kiotfpt.response.ShopResponse;
import com.kiotfpt.response.StatusResponse;
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

	@Autowired
	private BrandRepository brandRepository;

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
	

	public ResponseEntity<ResponseObject> updateProduct(int id, ProductRequest productRequest) {
		Optional<Product> product = repository.findById(id);

		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "product not exist", new int[0]));
		}
		String name = productRequest.getProduct_name(), description = productRequest.getProduct_description(),
				variants = productRequest.getProduct_variants();
		float price = productRequest.getProduct_price();
		int categoryId = productRequest.getCategory().getCategory_id(),
				brandId = productRequest.getBrand().getBrand_id(), shopId = productRequest.getShop().getShop_id(),
				p_repository = productRequest.getProduct_repository();

		// Validate input
		if (name == null || description == null || variants == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", new int[0]));
		}
		if (name.isEmpty() || description.isEmpty() || variants.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", new int[0]));
		}

		if (p_repository <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Repository must be greater than 0!", new int[0]));
		}

		// Check category existence
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Category does not exist!", new int[0]));
		}

		// Check shop existence
		Optional<Shop> shopOptional = shopRepository.findById(shopId);
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Shop does not exist!", new int[0]));
		}

		// Check brand existence
		Optional<Brand> brandOptional = brandRepository.findById(brandId);
		if (brandOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Brand does not exist!", new int[0]));
		}

		product.get().setProduct_name(name);
		product.get().setProduct_description(description);
		product.get().setProduct_price(price);
		product.get().setProduct_variants(variants);
		product.get().setCategory(categoryOptional.get());
		product.get().setShop(shopOptional.get());
		product.get().setProduct_best_seller(false);
		product.get().setProduct_popular(false);
		product.get().setProduct_sold(0);
		product.get().setProduct_repository(p_repository);

		product.get().setThumbnail(productRequest.getThumbnail());

		// Save the product
		repository.save(product.get());
		
		ProductResponse res = new ProductResponse(product.get());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product updated successfully", res));
	}

	public ResponseEntity<ResponseObject> deleteProduct(int id) {
		Optional<Product> pro = repository.findById(id);

		if (!pro.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "product not exist", new int[0]));
		}
		Optional<Status> statusDeleted = statusRepository.findByValue("Inactive");
		Product product = pro.get();
		product.setStatus(statusDeleted.get());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product is deleted successfully", repository.save(product)));
	}

	public ResponseEntity<ResponseObject> createProduct(ProductRequest productRequest) {
		// Retrieve values from the ProductRequest object
		String name = productRequest.getProduct_name(), description = productRequest.getProduct_description(),
				variants = productRequest.getProduct_variants();
		float price = productRequest.getProduct_price();
		int categoryId = productRequest.getCategory().getCategory_id(),
				brandId = productRequest.getBrand().getBrand_id(), shopId = productRequest.getShop().getShop_id(),
				p_repository = productRequest.getProduct_repository();

		// Validate input
		if (name == null || description == null || variants == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", new int[0]));
		}
		if (name.isEmpty() || description.isEmpty() || variants.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", new int[0]));
		}

		if (p_repository <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Repository must be greater than 0!", new int[0]));
		}

		// Check category existence
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Category does not exist!", new int[0]));
		}

		// Check shop existence
		Optional<Shop> shopOptional = shopRepository.findById(shopId);
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Shop does not exist!", new int[0]));
		}

		// Check brand existence
		Optional<Brand> brandOptional = brandRepository.findById(brandId);
		if (brandOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Brand does not exist!", new int[0]));
		}
		Optional<Status> stat = statusRepository.findByValue("Active");
		
		// Create the product entity
		Product product = new Product();
		product.setProduct_name(name);
		product.setProduct_description(description);
		product.setProduct_price(price);
		product.setProduct_variants(variants);
		product.setCategory(categoryOptional.get());
		product.setShop(shopOptional.get());
		product.setProduct_best_seller(false);
		product.setProduct_popular(false);
		product.setProduct_sold(0);
		product.setProduct_repository(p_repository);
		product.setStatus(stat.get());
		product.setThumbnail(productRequest.getThumbnail());

		// Save the product
		repository.save(product);
		ProductResponse res = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product created successfully", res));
	}

	public ResponseEntity<ResponseObject> findByShopId(int id, int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findById(id);
		if (shopOptional.isPresent()) {
			Pageable pageable = PageRequest.of(page - 1, amount);
			Page<Product> productPage = repository.findAllByShopId(id, pageable);

			if (!productPage.isEmpty()) {
				List<Product> products = productPage.getContent();
				List<ProductResponse> returnListProduct = new ArrayList<>();

				for (Product product : products) {
					int status_id = product.getStatus().getStatus_id();
					if (status_id != 2 && status_id != 3 && status_id != 4) {
						Product_Condition condition = product.getProduct_condition();

						if (condition == null) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
											"Condition of product id: " + product.getProduct_id() + " not found", ""));
						}
						Brand brand = product.getBrand();

						if (brand == null) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
											"Brand of product id: " + product.getProduct_id() + "not found", ""));
						}
						Status status = product.getStatus();

						if (status == null) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
											"Status of product id: " + product.getProduct_id() + "not found", ""));
						}
						Category category = product.getCategory();

						if (category == null) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
											"Category of product id: " + product.getProduct_id() + "not found", ""));
						}
						Shop shop = product.getShop();

						if (shop == null) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
											"Shop of product id: " + product.getProduct_id() + "not found", ""));
						}

						List<Product_Thumbnail> thumbnails = product.getThumbnail();

						ProductResponse res = new ProductResponse(product.getProduct_id(), product.getProduct_name(),
								product.getProduct_description(), product.getProduct_sold(), product.getProduct_price(),
								product.isProduct_best_seller(), product.isProduct_popular(),
								product.getProduct_variants(), product.getProduct_repository(),
								new Product_ConditionResponse(condition.getPc_id(), condition.getPc_value()),
								new BrandResponse(brand.getBrand_id(), brand.getBrand_name(),
										brand.getBrand_thumbnail()),
								new StatusResponse(status.getStatus_id(), status.getValue()),
								new CategoryResponse(category.getCategory_id(), category.getCategory_name(),
										category.getCategory_thumbnail()),
								new ShopResponse(shop.getShop_id(), shop.getShop_name(), shop.getShop_email(),
										shop.getShop_phone(), shop.getShop_thumbnail(), null),
								thumbnails, null);
						returnListProduct.add(res);

					}
				}

				if (!returnListProduct.isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdSuccess"), returnListProduct));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
							HttpStatus.NOT_FOUND.toString().split(" ")[0], "No products", ""));
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "No products", ""));
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("notFindShop"), ""));
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
