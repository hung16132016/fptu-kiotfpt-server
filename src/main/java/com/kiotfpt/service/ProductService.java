package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.kiotfpt.exception.ResourceNotFoundException;
import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.Color;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductCondition;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Size;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Variant;
import com.kiotfpt.repository.BrandRepository;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ColorRepository;
import com.kiotfpt.repository.ConditionRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.SizeRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.BrandRequest;
import com.kiotfpt.request.CategoryRequest;
import com.kiotfpt.request.ProductRequest;
import com.kiotfpt.request.Product_ConditionRequest;
import com.kiotfpt.request.ShopRequest;
import com.kiotfpt.request.StatusRequest;
import com.kiotfpt.request.VariantRequest;
import com.kiotfpt.response.BrandResponse;
import com.kiotfpt.response.CategoryResponse;
import com.kiotfpt.response.ProductResponse;
import com.kiotfpt.response.Product_ConditionResponse;
import com.kiotfpt.response.ShopResponse;
import com.kiotfpt.response.StatusResponse;
import com.kiotfpt.response.VariantResponse;
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

	@Autowired
	private ConditionRepository conditionRepository;

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private SizeRepository sizeRepository;

//	@Value("${path_image}")
//	private String imageLink;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	// fix
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

	// fix
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

	// fix
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

	// fix
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
		Optional<Product> productOptional = repository.findById(id);

		if (productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", "Product not found", null));
		}

		Product product = productOptional.get();

		// Update product details
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setMinPrice(productRequest.getMinPrice());
		product.setMaxPrice(productRequest.getMaxPrice());
		product.setRate(productRequest.getRate());
		product.setBestSeller(productRequest.isBestSeller());
		product.setPopular(productRequest.isPopular());
		product.setTopDeal(productRequest.isTopDeal());
		product.setOfficial(productRequest.isOfficial());

		// Set product relationships
		product.setCondition(conditionRepository.findById(productRequest.getCondition().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Condition not found")));
		product.setBrand(brandRepository.findById(productRequest.getBrand().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
		product.setStatus(statusRepository.findById(productRequest.getStatus().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Status not found")));
		product.setCategory(categoryRepository.findById(productRequest.getCategory().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found")));
		product.setShop(shopRepository.findById(productRequest.getShop().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Shop not found")));

		// Update variants
		List<VariantRequest> variantRequests = (List<VariantRequest>) productRequest.getVariants();
		List<Variant> variants = new ArrayList<>();

		for (VariantRequest variantRequest : variantRequests) {
			Variant variant = new Variant();
			variant.setPrice(variantRequest.getPrice());
			variant.setQuantity(variantRequest.getQuantity());
			variant.setColor(colorRepository.findById(variantRequest.getColorId())
					.orElseThrow(() -> new ResourceNotFoundException("Color not found")));
			variant.setSize(sizeRepository.findById(variantRequest.getSizeId())
					.orElseThrow(() -> new ResourceNotFoundException("Size not found")));
			variant.setProduct(product);
			variants.add(variant);
		}

		product.setVariants(variants);

		// Save the updated product
		repository.save(product);

		ProductResponse response = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, "200", "Product updated successfully", response));
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

	// Fixing
	public ResponseEntity<ResponseObject> createProduct(ProductRequest productRequest) {
		// Retrieve values from the ProductRequest object
		String name = productRequest.getName();
		String description = productRequest.getDescription();
		float minPrice = productRequest.getMinPrice();
		float maxPrice = productRequest.getMaxPrice();
		float rate = productRequest.getRate();
		boolean bestSeller = productRequest.isBestSeller();
		boolean popular = productRequest.isPopular();
		boolean topDeal = productRequest.isTopDeal();
		boolean official = productRequest.isOfficial();
		Product_ConditionRequest condition = productRequest.getCondition();
		BrandRequest brand = productRequest.getBrand();
		StatusRequest status = productRequest.getStatus();
		CategoryRequest category = productRequest.getCategory();
		ShopRequest shop = productRequest.getShop();
		Collection<VariantRequest> variantRequests = productRequest.getVariants();

		// Validate input
		if (name == null || description == null || variantRequests == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input cannot be empty!", null));
		}

		if (name.isEmpty() || description.isEmpty() || variantRequests.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input cannot be empty!", null));
		}

		// Check category existence
		Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
		if (categoryOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Category does not exist!", null));
		}

		// Check shop existence
		Optional<Shop> shopOptional = shopRepository.findById(shop.getId());
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Shop does not exist!", null));
		}

		// Check brand existence
		Optional<Brand> brandOptional = brandRepository.findById(brand.getId());
		if (brandOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Brand does not exist!", null));
		}

		// Check status existence
		Optional<Status> statusOptional = statusRepository.findById(status.getId());
		if (statusOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Status does not exist!", null));
		}

		// Check condition existence
		Optional<ProductCondition> conditionOptional = conditionRepository.findById(condition.getId());
		if (conditionOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Product condition does not exist!", null));
		}

		// Create the product entity
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setMinPrice(minPrice);
		product.setMaxPrice(maxPrice);
		product.setRate(rate);
		product.setBestSeller(bestSeller);
		product.setPopular(popular);
		product.setTopDeal(topDeal);
		product.setOfficial(official);
		product.setCondition(conditionOptional.get());
		product.setBrand(brandOptional.get());
		product.setStatus(statusOptional.get());
		product.setCategory(categoryOptional.get());
		product.setShop(shopOptional.get());

		// Set variants
		List<Variant> variants = new ArrayList<>();
		for (VariantRequest variantRequest : variantRequests) {
			Variant variant = new Variant();
			variant.setPrice(variantRequest.getPrice());
			variant.setQuantity(variantRequest.getQuantity());

			// Fetch color entity from repository
			Optional<Color> colorOptional = colorRepository.findById(variantRequest.getColorId());
			if (colorOptional.isPresent()) {
				variant.setColor(colorOptional.get());
			} else {
				// Return a 404 response if color is not found
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, "404", "Color not found!", null));
			}

			// Fetch size entity from repository
			Optional<Size> sizeOptional = sizeRepository.findById(variantRequest.getSizeId());
			if (sizeOptional.isPresent()) {
				variant.setSize(sizeOptional.get());
			} else {
				// Return a 404 response if size is not found
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, "404", "Size not found!", null));
			}

			// Add the variant to the list
			variants.add(variant);
		}
		product.setVariants(variants);

		// Save the product
		repository.save(product);
		ProductResponse response = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, "200", "Product created successfully", response));
	}

	// fix
	public ResponseEntity<ResponseObject> findByShopId(int id, int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findById(id);
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", responseMessage.get("notFindShop"), null));
		}

		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findAllByShopId(id, pageable);

		if (productPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", "No products", null));
		}

		List<Product> products = productPage.getContent();
		List<ProductResponse> returnListProduct = new ArrayList<>();

		for (Product product : products) {
			int statusId = product.getStatus().getId();
			if (statusId != 2 && statusId != 3 && statusId != 4) {
				List<Variant> variants = (List<Variant>) product.getVariants();
				List<VariantResponse> variantResponses = new ArrayList<>();
				for (Variant variant : variants) {
					variantResponses.add(new VariantResponse(variant));
				}

				ProductResponse res = new ProductResponse();
				res.setId(product.getId());
				res.setSold(product.getSold());
				res.setDiscount(product.getDiscount());
				res.setName(product.getName());
				res.setDescription(product.getDescription());
				res.setMinPrice(product.getMinPrice());
				res.setMaxPrice(product.getMaxPrice());
				res.setRate(product.getRate());
				res.setBestSeller(product.isBestSeller());
				res.setPopular(product.isPopular());
				res.setTopDeal(product.isTopDeal());
				res.setOfficial(product.isOfficial());
				res.setCondition(new Product_ConditionResponse(product.getCondition()));
				res.setBrand(new BrandResponse(product.getBrand()));
				res.setStatus(new StatusResponse(product.getStatus()));
				res.setCategory(new CategoryResponse(product.getCategory()));
				res.setShop(new ShopResponse(product.getShop()));
				res.setVariants(variantResponses);

				returnListProduct.add(res);
			}
		}

		if (!returnListProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
							responseMessage.get("getProductByShopIdSuccess"), returnListProduct));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "No products", null));
		}
	}

	// fix
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
					int status = product.getStatus().getId();
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

	// fix
	public List<Product> searchByName(@PathVariable(name = "name") String name) {
		List<Product> found = repository.findByname(name);
		return !found.isEmpty() ? repository.findByname(name) : null;
	}
	
	public ResponseEntity<ResponseObject> getTopDealProduct() {
		List<Product> products = repository.findByTopDeal();
		
		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	
	public ResponseEntity<ResponseObject> getTopDealProductWithShopId(int shop_id) {
		List<Product> products = repository.findByTopDealAndShopId(shop_id);
		
		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getOfficialProducts() {
		List<Product> officialProducts = repository.findByOfficialTrue();

		if (officialProducts == null || officialProducts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No official products found.", null));
		}

		List<ProductResponse> officialProductResponses = officialProducts.stream().map(ProductResponse::new)
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Official products found.", officialProductResponses));
	}
	
	public ResponseEntity<ResponseObject> getOfficialProductsWithShopID(int shop_id) {
		List<Product> officialProducts = repository.findByShopIdAndOfficialTrue(shop_id);

		if (officialProducts == null || officialProducts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No official products found.", null));
		}

		List<ProductResponse> officialProductResponses = officialProducts.stream().map(ProductResponse::new)
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Official products found.", officialProductResponses));
	}

	public ResponseEntity<ResponseObject> getDiscountedProducts() {
		List<Product> discountedProducts = repository.findByDiscountGreaterThan(0);

		if (discountedProducts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No discounted products found.", null));
		}

		List<ProductResponse> discountedProductResponses = discountedProducts.stream().map(ProductResponse::new)
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Discounted products found.", discountedProductResponses));
	}
	
	public ResponseEntity<ResponseObject> getDiscountedProductsWithShopID(int shop_id) {
		List<Product> discountedProducts = repository.findByShopIdAndDiscountGreaterThan(shop_id, 0);

		if (discountedProducts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "No discounted products found.", null));
		}

		List<ProductResponse> discountedProductResponses = discountedProducts.stream().map(ProductResponse::new)
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Discounted products found.", discountedProductResponses));
	}
	
	public ResponseEntity<ResponseObject> getProductRelated(int product_id) {
		Optional<Product> product = repository.findById(product_id);
		if (!product.isEmpty()) {
			Category cate = product.get().getCategory();
			
			List<Product> products = repository.findTop6ByCategoryAndIdNot(cate, product_id);
			if (products.isEmpty()) 
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "There is no product related to this product!", null));
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Discounted products found.", products));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], "There is no product with this product id!", null));
	}
	
	public ResponseEntity<ResponseObject> getByPriceRange(float minPrice, float maxPrice, int page, int amount) {
		if (minPrice > maxPrice) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Min price must be less than or equal Max price!", null));
		}
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findByPriceRange(minPrice, maxPrice, pageable);
		
		List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found with this range", new int[0]));
		
	}
	
	public ResponseEntity<ResponseObject> getByListCategoryID(List<String> categoryIdList) {
		if (categoryIdList.isEmpty()) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "This list has at least one category ID!", null));
		
		List<Product> allProducts = new ArrayList<>();
		for (String categoryId : categoryIdList) {
            List<Product> productsByCategory = repository.findAllByCategoryid(Integer.parseInt(categoryId));
            allProducts.addAll(productsByCategory);
        }
		
		return !allProducts.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", allProducts))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found with this range", new int[0]));
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
//			Collections.sort(products, (p1, p2) -> Double.compare(Double.parseDouble(p1.getPrice()), Double.parseDouble(p2.getPrice())));
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//							"Data has found successfully", products));
//		} else if (price.equals("high-to-low")) {
//			Collections.sort(products, (p1, p2) -> Double.compare(Double.parseDouble(p2.getPrice()), Double.parseDouble(p1.getPrice())));
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
