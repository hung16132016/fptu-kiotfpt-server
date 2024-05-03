package com.kiotfpt.request;

import com.kiotfpt.model.Product_Condition;

public class Product_ConditionRequest {

	private int pc_id;

	private String pc_value;

	public Product_ConditionRequest() {
		super();
	}

	public Product_ConditionRequest(int pc_id, String pc_value) {
		super();
		this.pc_id = pc_id;
		this.pc_value = pc_value;
	}
	
	public Product_ConditionRequest(Product_Condition condition) {
		super();
		this.pc_id = condition.getPc_id();
		this.pc_value = condition.getPc_value();
	}

	public int getPc_id() {
		return pc_id;
	}

	public void setPc_id(int pc_id) {
		this.pc_id = pc_id;
	}

	public String getPc_value() {
		return pc_value;
	}

	public void setPc_value(String pc_value) {
		this.pc_value = pc_value;
	}
	
}
