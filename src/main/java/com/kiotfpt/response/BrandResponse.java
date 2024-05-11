package com.kiotfpt.response;

import com.kiotfpt.model.Brand;

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

	public BrandResponse(Brand brand) {
		super();
		this.brand_id = brand.getId();
		this.brand_name = brand.getName();
		this.brand_thumbnail = brand.getThumbnail();
	}

}
