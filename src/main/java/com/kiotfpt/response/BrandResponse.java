package com.kiotfpt.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {

	private int brand_id;
	private String brand_name;
	private String brand_thumbnail;
	private Collection<ProductResponse> products;

	public BrandResponse(Brand brand) {
		this.brand_id = brand.getId();
		this.brand_name = brand.getName();
		this.brand_thumbnail = brand.getThumbnail();
	}

	public BrandResponse(Brand brand, Collection<Product> products) {
		this.brand_id = brand.getId();
		this.brand_name = brand.getName();
		this.brand_thumbnail = brand.getThumbnail();

		List<ProductResponse> productList = new ArrayList<>();
		for (Product product : products) {
			ProductResponse productResponse = new ProductResponse(product);
			productList.add(productResponse);
		}
		this.products = productList;
	}

}
