package com.kiotfpt.response;

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

	private List<ProductThumbnail> thumbnail;

	private Collection<Comment> comments;

//	public ProductResponse(Product product) {
//		super();
//		this.product_id = product.getProduct_id();
//		this.product_name = product.getProduct_name();
//		this.product_description = product.getProduct_description();
//		this.product_sold = product.getProduct_sold();
//		this.product_price = product.getProduct_price();
//		this.product_best_seller = product.isProduct_best_seller();
//		this.product_popular = product.isProduct_popular();
//		this.product_variants = product.getProduct_variants();
//		this.product_repository = product.getProduct_repository();
//		this.product_condition = new Product_ConditionResponse(product.getProduct_condition());
//		this.brand = new BrandResponse(product.getBrand());
//		this.status = new StatusResponse(product.getStatus());
//		this.category = new CategoryResponse(product.getCategory());
//		this.shop = new ShopResponse(product.getShop());
//		this.thumbnail = product.getThumbnail();
//		this.comments = product.getComments();
//	}

}
