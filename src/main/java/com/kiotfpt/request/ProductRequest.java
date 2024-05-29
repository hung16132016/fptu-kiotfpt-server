package com.kiotfpt.request;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	private int  id;
	private int  discount;
	private String  name;
	private String  description;
	private float  minPrice;
	private float  maxPrice;
	private int  condition_id;
	private int brand_id;
	private int category_id;
	private int shop_id;
	private List<String> thumbnails;
	private Collection<VariantRequest> variants;
	
}
