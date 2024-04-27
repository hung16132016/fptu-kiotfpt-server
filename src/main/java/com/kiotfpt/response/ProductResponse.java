package com.kiotfpt.response;

import java.util.Collection;
import java.util.List;

import com.kiotfpt.model.Comment;
import com.kiotfpt.model.Product_Thumbnail;

public class ProductResponse {

	private int product_id;

	private String product_name;

	private String product_description;
	
	private int product_sold;

	private float product_price;

	private boolean product_best_seller;

	private boolean product_popular;

	private String product_variants;
	
	private int product_repository;
	
	private Product_ConditionResponse product_condition;

	private BrandResponse brand;

	private StatusResponse status;

	private CategoryResponse category;

	private ShopResponse shop;
	
	private List<Product_Thumbnail> thumbnail;
	
	private Collection<Comment> comments;

	public ProductResponse() {
		super();
	}

	public ProductResponse(int product_id, String product_name, String product_description, int product_sold,
			float product_price, boolean product_best_seller, boolean product_popular, String product_variants,
			int product_repository, Product_ConditionResponse product_condition, BrandResponse brand,
			StatusResponse status, CategoryResponse category, ShopResponse shop, List<Product_Thumbnail> thumbnail,
			Collection<Comment> comments) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_description = product_description;
		this.product_sold = product_sold;
		this.product_price = product_price;
		this.product_best_seller = product_best_seller;
		this.product_popular = product_popular;
		this.product_variants = product_variants;
		this.product_repository = product_repository;
		this.product_condition = product_condition;
		this.brand = brand;
		this.status = status;
		this.category = category;
		this.shop = shop;
		this.thumbnail = thumbnail;
		this.comments = comments;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public int getProduct_sold() {
		return product_sold;
	}

	public void setProduct_sold(int product_sold) {
		this.product_sold = product_sold;
	}

	public float getProduct_price() {
		return product_price;
	}

	public void setProduct_price(float product_price) {
		this.product_price = product_price;
	}

	public boolean isProduct_best_seller() {
		return product_best_seller;
	}

	public void setProduct_best_seller(boolean product_best_seller) {
		this.product_best_seller = product_best_seller;
	}

	public boolean isProduct_popular() {
		return product_popular;
	}

	public void setProduct_popular(boolean product_popular) {
		this.product_popular = product_popular;
	}

	public String getProduct_variants() {
		return product_variants;
	}

	public void setProduct_variants(String product_variants) {
		this.product_variants = product_variants;
	}

	public int getProduct_repository() {
		return product_repository;
	}

	public void setProduct_repository(int product_repository) {
		this.product_repository = product_repository;
	}

	public Product_ConditionResponse getProduct_condition() {
		return product_condition;
	}

	public void setProduct_condition(Product_ConditionResponse product_condition) {
		this.product_condition = product_condition;
	}

	public BrandResponse getBrand() {
		return brand;
	}

	public void setBrand(BrandResponse brand) {
		this.brand = brand;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public CategoryResponse getCategory() {
		return category;
	}

	public void setCategory(CategoryResponse category) {
		this.category = category;
	}

	public ShopResponse getShop() {
		return shop;
	}

	public void setShop(ShopResponse shop) {
		this.shop = shop;
	}

	public List<Product_Thumbnail> getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(List<Product_Thumbnail> thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}
	
}
