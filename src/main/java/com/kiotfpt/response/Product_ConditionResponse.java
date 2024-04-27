package com.kiotfpt.response;

public class Product_ConditionResponse {

	private int pc_id;

	private String pc_value;

	public Product_ConditionResponse() {
		super();
	}

	public Product_ConditionResponse(int pc_id, String pc_value) {
		super();
		this.pc_id = pc_id;
		this.pc_value = pc_value;
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
