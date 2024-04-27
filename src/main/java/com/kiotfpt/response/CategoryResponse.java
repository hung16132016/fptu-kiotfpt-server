package com.kiotfpt.response;

public class CategoryResponse {

	private int category_id;

	private String name;

	private String category_thumbnail;

	public CategoryResponse() {
		super();
	}

	public CategoryResponse(int category_id, String name, String category_thumbnail) {
		super();
		this.category_id = category_id;
		this.name = name;
		this.category_thumbnail = category_thumbnail;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory_thumbnail() {
		return category_thumbnail;
	}

	public void setCategory_thumbnail(String category_thumbnail) {
		this.category_thumbnail = category_thumbnail;
	}
	
}
