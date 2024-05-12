package com.kiotfpt.response;

import com.kiotfpt.model.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponse {

	private int id;
	private String value;

	public ColorResponse(Color color) {
		super();
		this.id = color.getId();
		this.value = color.getValue();
	}

}
