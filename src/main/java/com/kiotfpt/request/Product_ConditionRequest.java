package com.kiotfpt.request;

import com.kiotfpt.model.ProductCondition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product_ConditionRequest {

	private int pc_id;
	private String pc_value;

	public Product_ConditionRequest(ProductCondition condition) {
		super();
		this.pc_id = condition.getId();
		this.pc_value = condition.getValue();
	}

}
