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
public class ProductResponse {

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
	private ShopResponse shop;
	private Collection<VariantResponse> variants;
	private Collection<ProductThumbnail> thumbnail;

	public ProductResponse(Product product) {
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
		this.shop = new ShopResponse(product.getShop());

		List<VariantResponse> list = new ArrayList<VariantResponse>();
		for (Variant variant : product.getVariants()) {
			VariantResponse v = new VariantResponse(variant);
			list.add(v);
		}
		this.variants = list;
		this.thumbnail = product.getThumbnail();
	}

}
