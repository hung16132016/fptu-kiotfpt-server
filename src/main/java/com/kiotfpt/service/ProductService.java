package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.Color;
import com.kiotfpt.model.Comment;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductCondition;
import com.kiotfpt.model.ProductThumbnail;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.ShopCategory;
import com.kiotfpt.model.Size;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Variant;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.BrandRepository;
import com.kiotfpt.repository.CategoryRepository;
import com.kiotfpt.repository.ColorRepository;
import com.kiotfpt.repository.CommentRepository;
import com.kiotfpt.repository.ConditionRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ProductThumbnailRepository;
import com.kiotfpt.repository.ShopCateRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.SizeRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.VariantRepository;
import com.kiotfpt.request.DateRequest;
import com.kiotfpt.request.ProductRequest;
import com.kiotfpt.request.VariantRequest;
import com.kiotfpt.response.ProductMiniResponse;
import com.kiotfpt.response.ProductResponse;
import com.kiotfpt.response.ProductShopResponse;
import com.kiotfpt.response.ProductStatisResponse;
import com.kiotfpt.response.ProfileMiniResponse;
import com.kiotfpt.response.VariantResponse;
import com.kiotfpt.utils.DateUtil;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@Autowired
	private VariantRepository variantRepository;

	@Autowired
	private ProductThumbnailRepository thumbnailRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private AccountProfileRepository accountProfileRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ShopCateRepository shopCategoryRepository;

	@Autowired
	private TokenUtils tokenUtils;

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

	public ResponseEntity<ResponseObject> getProductById(int productId) {
		Optional<Product> productOpt = repository.findById(productId);

		if (!productOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Product not found", null));
		}

		Product product = productOpt.get();
		List<Comment> comments = commentRepository.findAllByProduct(product);

		// Populate ProfileMiniResponse for each comment
		comments.forEach(comment -> {
			ProfileMiniResponse profile = fetchProfileForComment(comment.getAccount());
			comment.setProfile(profile);
		});

		// Set comments back to product
		product.setComments(comments);

	    List<ProfileMiniResponse> profiles = product.getFavourite().stream()
	            .map(fav -> fetchProfileForAccount(fav.getAccount()))
	            .collect(Collectors.toList());

		ProductDetailResponse res = new ProductDetailResponse(product, profiles);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Product found", res));
	}

	private ProfileMiniResponse fetchProfileForAccount(Account account) {
	    // Similar to fetchProfileForComment, fetch the ProfileMiniResponse for the given Account
	    Optional<AccountProfile> profileOpt = accountProfileRepository.findByAccount(account);
	    return profileOpt.map(ProfileMiniResponse::new).orElse(null);
	}
	
	private ProfileMiniResponse fetchProfileForComment(Account account) {
		Optional<AccountProfile> profile = accountProfileRepository.findByAccount(account);
		return new ProfileMiniResponse(profile.get());
	}

	public ResponseEntity<ResponseObject> getByCategoryID(int category_id, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findByStatus11AndCategoryId(pageable, category_id);
		List<Product> products = productPage.getContent();
		ListProduct list = new ListProduct(productPage.getTotalPages(), products);

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", list))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getByBrandID(int brand_id, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findByStatus11AndBrandId(pageable, brand_id);
		List<Product> products = productPage.getContent();
		ListProduct list = new ListProduct(productPage.getTotalPages(), products);

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", list))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getProductsByRate(float rate, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		List<Product> products = repository.findByRate(rate, pageable);

		return !products.isEmpty()
				? ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Data has found successfully", products)
				: ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Data has not found");
	}

	public ResponseEntity<ResponseObject> getByKeyword(String keyword, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findByStatus11AndKeyword(pageable, keyword);

		List<Product> products = productPage.getContent();
		List<Shop> shops = shopRepository.findByNameContainingIgnoreCase(keyword);

		SearchProductResponse response = new SearchProductResponse(productPage.getTotalPages(), products, shops);

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", response))
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

		// Validate input
		if (productRequest.getName() == null || productRequest.getDescription() == null
				|| productRequest.getVariants() == null || productRequest.getThumbnails() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", null));
		}

		if (productRequest.getName().isEmpty() || productRequest.getDescription().isEmpty()
				|| productRequest.getVariants().isEmpty() || productRequest.getThumbnails().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", null));
		}

		int discount = productRequest.getDiscount();

		if (discount < 0 || discount > 100) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Discount must be between 0 and 100!", null));
		}

		// Update product details
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setDiscount(discount);

		// Set product relationships
		try {
			product.setCondition(conditionRepository.findById(productRequest.getCondition_id())
					.orElseThrow(() -> new ResourceNotFoundException("Condition not found")));
			product.setBrand(brandRepository.findById(productRequest.getBrand_id())
					.orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
			product.setCategory(categoryRepository.findById(productRequest.getCategory_id())
					.orElseThrow(() -> new ResourceNotFoundException("Category not found")));
			product.setShop(shopRepository.findById(productRequest.getShop_id())
					.orElseThrow(() -> new ResourceNotFoundException("Shop not found")));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], e.getMessage(), null));
		}

		// Update variants
		List<VariantRequest> variantRequests = (List<VariantRequest>) productRequest.getVariants();
		List<Variant> updatedVariants = new ArrayList<>();
		float minPrice = Float.MAX_VALUE;
		float maxPrice = Float.MIN_VALUE;

		for (VariantRequest variantRequest : variantRequests) {
			Optional<Variant> existingVariantOpt = variantRepository.findByProductIdAndColorIdAndSizeId(product.getId(),
					variantRequest.getColorId(), variantRequest.getSizeId());

			Variant variant;
			if (existingVariantOpt.isPresent()) {
				// Update existing variant
				variant = existingVariantOpt.get();
				variant.setPrice(variantRequest.getPrice());
				variant.setQuantity(variantRequest.getQuantity());
			} else {
				// Add new variant
				variant = new Variant();
				variant.setPrice(variantRequest.getPrice());
				variant.setQuantity(variantRequest.getQuantity());
				variant.setColor(colorRepository.findById(variantRequest.getColorId())
						.orElseThrow(() -> new ResourceNotFoundException("Color not found")));
				variant.setSize(sizeRepository.findById(variantRequest.getSizeId())
						.orElseThrow(() -> new ResourceNotFoundException("Size not found")));
				variant.setProduct(product);
			}

			float variantPrice = variantRequest.getPrice();
			minPrice = Math.min(minPrice, variantPrice);
			maxPrice = Math.max(maxPrice, variantPrice);

			updatedVariants.add(variant);
		}

		product.setVariants(updatedVariants);
		product.setMinPrice(minPrice);
		product.setMaxPrice(maxPrice);

		// Update thumbnails
		List<String> thumbnailUrls = productRequest.getThumbnails();
		List<ProductThumbnail> thumbnails = new ArrayList<>();

		for (String thumbnailUrl : thumbnailUrls) {
			ProductThumbnail thumbnail = new ProductThumbnail(thumbnailUrl, product);
			thumbnails.add(thumbnail);
		}

		product.setThumbnail(thumbnails);

		// Save the updated product
		repository.save(product);

		ProductResponse response = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product updated successfully", response));
	}

	public ResponseEntity<ResponseObject> deleteProduct(int productId, List<Integer> variantIds) {
		Optional<Product> pro = repository.findById(productId);

		if (!pro.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Product not found", new int[0]));
		}

		Product product = pro.get();
		Collection<Variant> productVariants = product.getVariants();

		// Check if all variants are selected for deletion
		if (variantIds.size() == productVariants.size()) {
			Optional<Status> statusDeleted = statusRepository.findByValue("inactive");
			if (!statusDeleted.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Inactive status not found", new int[0]));
			}
			product.setStatus(statusDeleted.get());
			repository.save(product);
		} else
			for (Integer variantId : variantIds) {
				variantRepository.deleteById(variantId);
			}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product or selected variants deleted successfully", null));
	}

	// Fixing
	public ResponseEntity<ResponseObject> createProduct(ProductRequest productRequest) {
		// Retrieve values from the ProductRequest object
		String name = productRequest.getName();
		String description = productRequest.getDescription();
		int discount = productRequest.getDiscount();
		int conditionId = productRequest.getCondition_id();
		int brandId = productRequest.getBrand_id();
		int categoryId = productRequest.getCategory_id();
		int shopId = productRequest.getShop_id();
		Collection<VariantRequest> variantRequests = productRequest.getVariants();
		List<String> thumbnailUrls = productRequest.getThumbnails();

		// Validate input
		if (name == null || description == null || variantRequests == null || thumbnailUrls == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", null));
		}

		if (name.isEmpty() || description.isEmpty() || variantRequests.isEmpty() || thumbnailUrls.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input cannot be empty!", null));
		}

		// Validate discount
		if (discount < 0 || discount > 100) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Discount must be between 0 and 100!", null));
		}

		// Check category existence
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Category does not exist!", null));
		}

		// Check shop existence
		Optional<Shop> shopOptional = shopRepository.findById(shopId);
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Shop does not exist!", null));
		}

		// Check brand existence
		Optional<Brand> brandOptional = brandRepository.findById(brandId);
		if (brandOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Brand does not exist!", null));
		}

		// Check status existence
		Optional<Status> statusOptional = statusRepository.findByValue("active");
		if (statusOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Status does not exist!", null));
		}

		// Check condition existence
		Optional<ProductCondition> conditionOptional = conditionRepository.findById(conditionId);
		if (conditionOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Product condition does not exist!", null));
		}

		// Create the product entity
		Product product = new Product(productRequest, conditionOptional.get(), brandOptional.get(),
				statusOptional.get(), categoryOptional.get(), shopOptional.get(), new ArrayList<>(), new ArrayList<>());

		// Save the product first to get its ID for the variants
		product = repository.save(product);

		// Set and save variants
		List<Variant> variants = new ArrayList<>();
		float minPrice = Float.MAX_VALUE;
		float maxPrice = Float.MIN_VALUE;

		for (VariantRequest variantRequest : variantRequests) {
			// Fetch color entity from repository
			Optional<Color> colorOptional = colorRepository.findById(variantRequest.getColorId());
			if (colorOptional.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Color not found!", null));
			}

			// Fetch size entity from repository
			Optional<Size> sizeOptional = sizeRepository.findById(variantRequest.getSizeId());
			if (sizeOptional.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Size not found!", null));
			}

			float variantPrice = variantRequest.getPrice();
			if (variantPrice <= 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Price must be greater than 0", null));

			minPrice = Math.min(minPrice, variantPrice);
			maxPrice = Math.max(maxPrice, variantPrice);

			Variant variant = new Variant(variantPrice, variantRequest.getQuantity(), colorOptional.get(),
					sizeOptional.get(), product);

			// Save the variant
			variant = variantRepository.save(variant);

			// Add the variant to the list
			variants.add(variant);

		}

		// Update the product with the saved variants
		product.setVariants(variants);
		product.setMinPrice(minPrice);
		product.setMaxPrice(maxPrice);

		// Set and save thumbnails
		List<ProductThumbnail> thumbnails = new ArrayList<ProductThumbnail>();
		for (String thumbnailUrl : thumbnailUrls) {
			ProductThumbnail thumbnail = new ProductThumbnail(thumbnailUrl, product);

			// Save each thumbnail
			thumbnailRepository.save(thumbnail);

			// Add the thumbnail to the list
			thumbnails.add(thumbnail);
		}

		ProductResponse response = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product created successfully", response));
	}

	// fix
	public ResponseEntity<ResponseObject> findByShopId(int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findByAccount(tokenUtils.getAccount());
		if (shopOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", responseMessage.get("notFindShop"), null));
		}

		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findAllByShopId(shopOptional.get().getId(), pageable);

		if (productPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", "No products", null));
		}

		List<Product> products = productPage.getContent();
		List<ProductShopResponse> returnListProduct = new ArrayList<>();

		for (Product product : products) {
			List<Variant> variants = (List<Variant>) product.getVariants();
			List<VariantResponse> variantResponses = new ArrayList<>();
			for (Variant variant : variants) {
				variantResponses.add(new VariantResponse(variant));
			}

			ProductShopResponse res = new ProductShopResponse(product);
			returnListProduct.add(res);

		}

		if (!returnListProduct.isEmpty()) {
			List<Product> lis = repository.findAllByShop(shopOptional.get());
			ProductShopRes res = new ProductShopRes((int) Math.ceil((double) lis.size() / amount), returnListProduct);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getProductByShopIdSuccess"), res));
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

	public ResponseEntity<ResponseObject> getProductRelated(int product_id) {
		Optional<Product> product = repository.findById(product_id);
		if (!product.isEmpty()) {
			Category cate = product.get().getCategory();

			List<Product> products = repository.findTop6ByCategoryAndIdNot(cate, product_id);
			if (products.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"There is no product related to this product!", null));

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Discounted products found.", products));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], "There is no product with this product id!", null));
	}

	public ResponseEntity<ResponseObject> getByPriceRange(float minPrice, float maxPrice, int page, int amount) {
		if (minPrice > maxPrice) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							"Min price must be less than or equal Max price!", null));
		}
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = repository.findByPriceRange(minPrice, maxPrice, pageable);

		List<Product> products = productPage.getContent();

		return !products.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", products))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"Data has not found with this range", new int[0]));

	}

	public ResponseEntity<ResponseObject> getByListCategoryID(List<String> categoryIdList) {
		if (categoryIdList.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "This list has at least one category ID!", null));

		List<Product> allProducts = new ArrayList<>();
		for (String categoryId : categoryIdList) {
			List<Product> productsByCategory = repository.findAllByCategoryid(Integer.parseInt(categoryId));
			allProducts.addAll(productsByCategory);
		}

		return !allProducts.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", allProducts))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"Data has not found with this range", new int[0]));
	}

	public ResponseEntity<ResponseObject> getTotalPage() {
		List<Product> products = repository.findAllActiveProduct();
		int number_of_page = (int) Math.ceil((double) products.size() / 10);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Data has found successfully", number_of_page));
	}

	public ResponseEntity<ResponseObject> filterProductsByTimeAndShop(DateRequest filterRequest) {
		try {

			Date startDate = DateUtil.calculateStartDate(filterRequest),
					endDate = DateUtil.calculateEndDate(filterRequest);

			List<Order> orders = null;
			if (tokenUtils.checkMatch("admin")) {
				orders = orderRepository.findByTimeCompleteBetweenAndStatusValue(startDate, endDate, "completed");
			} else if (tokenUtils.checkMatch("shop")) {

				Optional<Shop> shop = shopRepository.findByAccount(tokenUtils.getAccount());
				if (shop.isEmpty()) {
					return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
							"No shop found with the current account");
				}

				orders = orderRepository.findByTimeCompleteBetweenAndShopIdAndStatusValue(startDate, endDate,
						shop.get().getId(), "completed");
			}

			if (orders.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"No orders found for the given date range", null));
			}

			List<ProductStatisResponse> responseList = new ArrayList<>();
			ProductFilterResult result = new ProductFilterResult(responseList, 0, 0);

			for (Order order : orders) {
				for (AccessibilityItem item : order.getSection().getItems()) {
					responseList.add(new ProductStatisResponse(order.getId(), item.getVariant().getProduct().getName(),
							item.getVariant().getProduct().getThumbnail(), item.getQuantity(), order.getTotal(),
							order.getTimeComplete(), new VariantResponse(item.getVariant())));
					result.setTotalMoney(result.getTotalMoney() + item.getTotal());
					result.setTotalQuantity(result.getTotalQuantity() + item.getQuantity());
				}
			}
			result.setProducts(responseList);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Products filtered successfully", result));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"An error occurred while filtering products", null));
		}
	}

	public ResponseEntity<ResponseObject> getProductsNotCommentedByAccount(int accountId) {
		// Fetch completed orders for the account
		List<Order> completedOrders = orderRepository.findByAccountIdAndStatusId(accountId, 27);

		// Collect all products in the completed orders
		List<ProductMiniResponse> productsWithoutComments = completedOrders.stream()
				.flatMap(order -> order.getSection().getItems().stream()).map(AccessibilityItem::getVariant)
				.filter(variant -> !commentRepository.existsByAccountIdAndProductId(accountId,
						variant.getProduct().getId()))
				.map(variant -> new ProductMiniResponse(variant.getProduct(), variant)).collect(Collectors.toList());

		if (productsWithoutComments.isEmpty()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "No products without comments");
		}

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK,
				"Products without comments retrieved successfully", productsWithoutComments);
	}

	public ResponseEntity<ResponseObject> getProductsByShopCatID(int shopCatID, int page, int amount) {
		Optional<ShopCategory> shopCate = shopCategoryRepository.findById(shopCatID);

		if (!shopCate.isEmpty()) {
			Optional<Shop> shop = shopRepository.findById(shopCate.get().getShop().getId());
			Optional<Category> category = categoryRepository.findById(shopCate.get().getCategory().getId());
			if (shop.isEmpty() || category.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"There is no category or no shop with this shop category!", null));
			}
			Pageable pageable = PageRequest.of(page - 1, amount);
			Page<Product> productPage = repository.findByShopIDAndCateID(shopCate.get().getShop().getId(),
					shopCate.get().getCategory().getId(), pageable);
			if (productPage.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject(false, "404", "There are no product with this shop category id!", null));
			}

			List<Product> products = productPage.getContent();
			List<ProductShopResponse> returnListProduct = new ArrayList<>();

			for (Product product : products) {
				int statusId = product.getStatus().getId();
				if (statusId == 11) {
					ProductShopResponse res = new ProductShopResponse(product);
					returnListProduct.add(res);
				}
			}
			if (!returnListProduct.isEmpty()) {
				ProductShopRes res = new ProductShopRes(productPage.getTotalPages(), returnListProduct);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								responseMessage.get("Get product by shop category id successfull"), res));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "No products", null));
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						"There is no shop category with this shop category id!", null));

	}

	public ResponseEntity<ResponseObject> getProductsByType(String type, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = Page.empty();

		if (type.equals("popular")) {
			productPage = repository.findByPopular(pageable);
		} else if (type.equals("best-seller")) {
			productPage = repository.findByBestSeller(pageable);
		} else if (type.equals("official")) {
			productPage = repository.findByOfficial(pageable);
		} else if (type.equals("top-deal")) {
			productPage = repository.findByTopDeal(pageable);
		} else if (type.equals("discount")) {
			productPage = repository.findByDiscountGreaterThan(0, pageable);
		} else if (type.equals("all")) {
			Optional<Status> active = statusRepository.findByValue("active");
			productPage = repository.findByStatus(pageable, active.get());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "The type is invalid!", null));
		}

		if (productPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", "There are no product with this shop category id!", null));
		}
		List<Product> products = productPage.getContent();
		List<ProductShopResponse> returnListProduct = new ArrayList<>();

		for (Product product : products) {
			int statusId = product.getStatus().getId();
			if (statusId == 11) {

				List<AccountProfile> profiles = product.getFavourite().stream()
						.map(fav -> accountProfileRepository.findByAccount(fav.getAccount()))
						.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

				ProductShopResponse res = new ProductShopResponse(product, profiles);
				returnListProduct.add(res);
			}
		}
		if (!returnListProduct.isEmpty()) {
			ProductShopRes res = new ProductShopRes(productPage.getTotalPages(), returnListProduct);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
							responseMessage.get("Get product by type successfull"), res));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "There are no products with this type", null));
		}
	}

	public ResponseEntity<ResponseObject> getProductsByTypeAndShopID(String type, int shopID, int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Product> productPage = Page.empty();

		Optional<Shop> shop = shopRepository.findById(shopID);
		if (shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "There is no shop with this shop id!", null));
		}

		if (type.equals("popular")) {
			productPage = repository.findByPopularAndShopId(shopID, pageable);
		} else if (type.equals("best-seller")) {
			productPage = repository.findByBestSellerAndShopId(shopID, pageable);
		} else if (type.equals("official")) {
			productPage = repository.findByOfficialAndShopId(shopID, pageable);
		} else if (type.equals("top-deal")) {
			productPage = repository.findByTopDealAndShopId(shopID, pageable);
		} else if (type.equals("discount")) {
			productPage = repository.findByShopIdAndDiscountGreaterThan(shopID, 0, pageable);
		} else if (type.equals("all")) {
			productPage = repository.findAllByShopId(shopID, pageable);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "The type is invalid!", null));
		}

		if (productPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, "404", "There are no product with this shop category id!", null));
		}
		List<Product> products = productPage.getContent();
		List<ProductShopResponse> returnListProduct = new ArrayList<>();

		for (Product product : products) {
			ProductShopResponse res = new ProductShopResponse(product);
			returnListProduct.add(res);

		}
		if (!returnListProduct.isEmpty()) {
			ProductShopRes res = new ProductShopRes(productPage.getTotalPages(), returnListProduct);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
							responseMessage.get("Get product by type successfull"), res));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "There are no products with this type", null));
		}
	}

	public ResponseEntity<ResponseObject> getProductsWithReviews() {
		try {
			Optional<Shop> shop = shopRepository.findByAccount(tokenUtils.getAccount());
			if (shop.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
						"No shop found with the current account");
			}
			List<Product> products = repository.findAllByShopId(shop.get().getId());
			if (products.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
						"No products found for the given shop");
			}

			List<ProductReviewResponse> responseList = products.stream()
					.filter(product -> !commentRepository.findAllByProduct(product).isEmpty()).map(product -> {
						List<Comment> comments = commentRepository.findAllByProduct(product).stream().map(comment -> {
							AccountProfile profile = accountProfileRepository.findByAccount(comment.getAccount())
									.orElse(null);
							comment.setProfile(new ProfileMiniResponse(profile));
							return comment;
						}).collect(Collectors.toList());
						return new ProductReviewResponse(new ProductMiniResponse(product), comments);
					}).collect(Collectors.toList());

			if (responseList.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
						"No products with reviews found for the given shop");
			}

			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK,
					"Products with reviews found for the given shop", responseList);
		} catch (Exception e) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while fetching products with reviews for the given shop");
		}
	}

	// fix filter status
	public ResponseEntity<ResponseObject> getProductsBoughtByAccount() {
		try {
			List<Order> orders = orderRepository.findByAccountId(tokenUtils.getAccount().getId());

			List<ProductMiniResponse> productsBought = orders.stream()
					.flatMap(order -> order.getSection().getItems().stream())
					.map(item -> new ProductMiniResponse(item.getVariant().getProduct())).collect(Collectors.toList());

			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK,
					"Products bought by account retrieved successfully", productsBought);
		} catch (Exception e) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to retrieve products bought by account");
		}
	}

	public ResponseEntity<ResponseObject> deactiveProduct(int id) {

		Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		Status inactiveStatus = statusRepository.findByValue("inactive")
				.orElseThrow(() -> new RuntimeException("Inactive status not found"));
		product.setStatus(inactiveStatus);
		repository.save(product);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Product deleted successfully", null));
	}

	public ResponseEntity<ResponseObject> activateProduct(int productId) {
		Optional<Product> optionalProduct = repository.findByIdAndStatusValue(productId, "inactive");

		if (optionalProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Product not found or already active", null));
		}

		Product product = optionalProduct.get();
		Optional<Status> activeStatus = statusRepository.findByValue("active");

		if (activeStatus.isEmpty()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(false,
					HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0], "Active status not found", null));
		}

		product.setStatus(activeStatus.get());
		repository.save(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product activated successfully", null));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class ProductFilterResult {
		private List<ProductStatisResponse> products;
		private double totalMoney;
		private int totalQuantity;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ProductShopRes {
		private int totalPage;
		private List<ProductShopResponse> products;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ListProduct {
		private int totalPage;
		private List<Product> products;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class SearchProductResponse {
		private int totalPages;
		private List<Product> products;
		private List<Shop> shops;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class ProductReviewResponse {
		private ProductMiniResponse product;
		private List<Comment> comments;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class ProductDetailResponse {
		private Product product;
		private List<ProfileMiniResponse> profiles;
	}
}
