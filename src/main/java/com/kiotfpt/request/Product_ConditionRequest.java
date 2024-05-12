package com.kiotfpt.request;

import com.kiotfpt.model.ProductCondition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product_ConditionRequest {

	private int id;
	private String value;

	public Product_ConditionRequest(ProductCondition condition) {
		super();
		this.id = condition.getId();
		this.value = condition.getValue();
	}

}
