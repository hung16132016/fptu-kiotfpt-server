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

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Product found", product));
	}

	private ProfileMiniResponse fetchProfileForComment(Account account) {
		Optional<AccountProfile> profile = accountProfileRepository.findByAccount(account);
		return new ProfileMiniResponse(profile.get());
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
			Optional<Status> statusDeleted = statusRepository.findByValue("Inactive");
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
		List<ProductThumbnail> thumbnails = new ArrayList<>();
		for (String thumbnailUrl : thumbnailUrls) {
			ProductThumbnail thumbnail = new ProductThumbnail(thumbnailUrl, product);

			// Save each thumbnail
			thumbnail = thumbnailRepository.save(thumbnail);

			// Add the thumbnail to the list
			thumbnails.add(thumbnail);
		}

		// Update the product with the saved thumbnails
		product.setThumbnail(thumbnails);

		// Save the updated product
		product = repository.save(product);
		ProductResponse response = new ProductResponse(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Product created successfully", response));
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
		List<ProductShopResponse> returnListProduct = new ArrayList<>();

		for (Product product : products) {
			int statusId = product.getStatus().getId();
			if (statusId != 2 && statusId != 3 && statusId != 4) {
				List<Variant> variants = (List<Variant>) product.getVariants();
				List<VariantResponse> variantResponses = new ArrayList<>();
				for (Variant variant : variants) {
					variantResponses.add(new VariantResponse(variant));
				}

				ProductShopResponse res = new ProductShopResponse(product);
				returnListProduct.add(res);
			}
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

	public ResponseEntity<ResponseObject> filterProductsByTime(DateRequest filterRequest) {
		try {

			Date startDate = DateUtil.calculateStartDate(filterRequest),
					endDate = DateUtil.calculateEndDate(filterRequest);

			List<Order> orders = orderRepository.findByTimeCompleteBetweenAndStatusValue(startDate, endDate,
					"completed");

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
							item.getQuantity(), order.getTotal(), order.getTimeComplete(),
							new VariantResponse(item.getVariant())));
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

	public ResponseEntity<ResponseObject> filterProductsByTimeAndShop(DateRequest filterRequest, int shopId) {
		try {

			Date startDate = DateUtil.calculateStartDate(filterRequest),
					endDate = DateUtil.calculateEndDate(filterRequest);

			List<Order> orders = orderRepository.findByTimeCompleteBetweenAndShopIdAndStatusValue(startDate, endDate,
					shopId, "completed");

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
							item.getQuantity(), order.getTotal(), order.getTimeComplete(),
							new VariantResponse(item.getVariant())));
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
				.filter(variant -> !commentRepository.existsByAccountIdAndProductId(accountId, variant.getId()))
				.map(variant -> new ProductMiniResponse(variant.getProduct(), variant)).collect(Collectors.toList());

		if (productsWithoutComments.isEmpty()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "No products without comments");
		}

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK,
				"Products without comments retrieved successfully", productsWithoutComments);
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
}
