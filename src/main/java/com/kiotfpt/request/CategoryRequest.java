package com.kiotfpt.request;

import com.kiotfpt.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

	private int id;
	private String name;
	private String thumbnail;
	private int shop_id;

	public CategoryRequest(Category category) {
		super();
		this.id = category.getId();
		this.name = category.getName();
		this.thumbnail = category.getThumbnail();
	}

}
