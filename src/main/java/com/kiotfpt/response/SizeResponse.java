package com.kiotfpt.response;

import com.kiotfpt.model.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponse {
	
	private int id;
	private String value;

	public SizeResponse(Size size) {
		super();
		this.id = size.getId();
		this.value = size.getValue();
	}

}
