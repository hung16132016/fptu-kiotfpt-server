package com.kiotfpt.response;

import java.util.Collection;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductThumbnail;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Variant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMiniResponse {

	private int id;
	private String name;
	private float min_price;
	private float max_price;
	private float rate;
	private StatusResponse status;
	private Collection<ProductThumbnail> thumbnail;
	private VariantResponse variant;

	public ProductMiniResponse(Product product, AccessibilityItem item) {
		this.id = product.getId();
		this.name = product.getName();
		this.status = new StatusResponse(product.getStatus());
		this.thumbnail = product.getThumbnail();
	}

	public ProductMiniResponse(Product product, Shop Shop) {
		this.id = product.getId();
		this.name = product.getName();
		this.min_price = product.getMinPrice();
		this.max_price = product.getMaxPrice();
		this.rate = product.getRate();
		this.status = new StatusResponse(product.getStatus());
		this.thumbnail = product.getThumbnail();
	}

	public ProductMiniResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.status = new StatusResponse(product.getStatus());
		this.thumbnail = product.getThumbnail();
	}

	public ProductMiniResponse(Product product, Variant variant) {
		this.id = product.getId();
		this.name = product.getName();
		this.status = new StatusResponse(product.getStatus());
		this.thumbnail = product.getThumbnail();
		this.variant = new VariantResponse(variant);
	}
}
