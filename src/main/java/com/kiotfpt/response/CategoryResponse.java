package com.kiotfpt.response;

import com.kiotfpt.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

	private int category_id;
	private String name;
	private String category_thumbnail;


	
	public CategoryResponse(Category category) {
		super();
		this.category_id = category.getId();
		this.name = category.getName();
		this.category_thumbnail = category.getThumbnail();
	}

	
}
