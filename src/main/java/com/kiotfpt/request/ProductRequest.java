package com.kiotfpt.request;

import java.util.Collection;
import java.util.List;

import com.kiotfpt.model.Comment;
import com.kiotfpt.model.ProductThumbnail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	private int product_id;

	private String product_name;

	private String product_description;
	
	private int product_sold;

	private float product_price;

	private boolean product_best_seller;

	private boolean product_popular;

	private String product_variants;
	
	private int product_repository;
	
	private Product_ConditionRequest product_condition;

	private BrandRequest brand;

	private StatusRequest status;

	private CategoryRequest category;

	private ShopRequest shop;
	
	private List<ProductThumbnail> thumbnail;
	
	private Collection<Comment> comments;
	
//	public ProductRequest(Product product) {
//		super();
//		this.product_id = product.getId();
//		this.product_name = product.getName();
//		this.product_description = product.getDescription();
//		this.product_sold = product.getSold();
//		this.product_price = product.getProduct_price();
//		this.product_best_seller = product.isProduct_best_seller();
//		this.product_popular = product.isProduct_popular();
//		this.product_variants = product.getProduct_variants();
//		this.product_repository = product.getProduct_repository();
//		this.product_condition = new Product_ConditionRequest(product.getProduct_condition());
//		this.brand = new BrandRequest(product.getBrand());
//		this.status = new StatusRequest(product.getStatus());
//		this.category = new CategoryRequest(product.getCategory());
//		this.shop = new ShopRequest(product.getShop());
//		this.thumbnail = product.getThumbnail();
//		this.comments = product.getComments();
//	}

	
}
