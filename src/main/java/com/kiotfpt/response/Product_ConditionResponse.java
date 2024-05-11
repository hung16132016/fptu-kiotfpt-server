package com.kiotfpt.response;

import com.kiotfpt.model.ProductCondition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product_ConditionResponse {

	private int pc_id;
	private String pc_value;

	public Product_ConditionResponse(ProductCondition condition) {
		super();
		this.pc_id = condition.getId();
		this.pc_value = condition.getValue();
	}

}
