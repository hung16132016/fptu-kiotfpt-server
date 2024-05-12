package com.kiotfpt.request;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	private int  id;
	private int  sold;
	private int  discount;
	private String  name;
	private String  description;
	private float  minPrice;
	private float  maxPrice;
	private float  rate;
	private boolean  bestSeller;
	private boolean  popular;
	private boolean  topDeal;
	private boolean  official;
	private Product_ConditionRequest  condition;
	private BrandRequest brand;
	private StatusRequest status;
	private CategoryRequest category;
	private ShopRequest shop;
	private Collection<VariantRequest> variants;
	
}
