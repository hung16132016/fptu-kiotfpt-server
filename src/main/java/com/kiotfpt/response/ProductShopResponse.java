package com.kiotfpt.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductThumbnail;
import com.kiotfpt.model.Variant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductShopResponse {

	private int id;
	private int sold;
	private int discount;
	private String name;
	private String description;
	private float minPrice;
	private float maxPrice;
	private float rate;
	private boolean bestSeller;
	private boolean popular;
	private boolean topDeal;
	private boolean official;
	private Product_ConditionResponse condition;
	private BrandResponse brand;
	private StatusResponse status;
	private CategoryResponse category;
	private Collection<ProductThumbnail> thumbnail;
	private List<VariantResponse> variants;

	public ProductShopResponse(Product product) {
		super();
		this.id = product.getId();
		this.sold = product.getSold();
		this.discount = product.getDiscount();
		this.name = product.getName();
		this.description = product.getDescription();
		this.minPrice = product.getMinPrice();
		this.maxPrice = product.getMaxPrice();
		this.rate = product.getRate();
		this.bestSeller = product.isBestSeller();
		this.popular = product.isPopular();
		this.topDeal = product.isTopDeal();
		this.official = product.isOfficial();
		this.condition = new Product_ConditionResponse(product.getCondition());
		this.brand = new BrandResponse(product.getBrand());
		this.status = new StatusResponse(product.getStatus());
		this.category = new CategoryResponse(product.getCategory());
		this.thumbnail = product.getThumbnail();
		List<VariantResponse> listVariants = new ArrayList<>();
		for (Variant variant : product.getVariants()) {
			listVariants.add(new VariantResponse(variant));
		}
		this.variants = listVariants;
	}

}
