package com.kiotfpt.request;

import com.kiotfpt.model.Brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {

	private int brand_id;
	private String brand_name;
	private String brand_thumbnail;

	public BrandRequest(Brand brand) {
		super();
		this.brand_id = brand.getId();
		this.brand_name = brand.getName();
		this.brand_thumbnail = brand.getThumbnail();
	}

}
