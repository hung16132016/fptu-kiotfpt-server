package com.kiotfpt.response;

public class BrandResponse {

	private int brand_id;
	
	private String brand_name;
	
	private String brand_thumbnail;

	public BrandResponse() {
		super();
	}

	public int getBrand_id() {
		return brand_id;
	}

	public BrandResponse(int brand_id, String brand_name, String brand_thumbnail) {
		super();
		this.brand_id = brand_id;
		this.brand_name = brand_name;
		this.brand_thumbnail = brand_thumbnail;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBrand_thumbnail() {
		return brand_thumbnail;
	}

	public void setBrand_thumbnail(String brand_thumbnail) {
		this.brand_thumbnail = brand_thumbnail;
	}
	
}
