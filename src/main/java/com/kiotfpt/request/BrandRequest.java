package com.kiotfpt.request;

import com.kiotfpt.model.Brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {

	private int id;
	private String name;
	private String thumbnail;

	public BrandRequest(Brand brand) {
		super();
		this.id = brand.getId();
		this.name = brand.getName();
		this.thumbnail = brand.getThumbnail();
	}

}
