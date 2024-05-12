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
	private String category_thumbnail;

	public CategoryRequest(Category category) {
		super();
		this.id = category.getId();
		this.name = category.getName();
		this.category_thumbnail = category.getThumbnail();
	}

}
