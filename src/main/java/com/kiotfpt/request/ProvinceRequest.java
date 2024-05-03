package com.kiotfpt.request;

import com.kiotfpt.model.Province;

public class ProvinceRequest {

	private int province_id;
	private String province_value;

	public ProvinceRequest() {
	}

	public ProvinceRequest(int province_id, String province_value) {
		this.province_id = province_id;
		this.province_value = province_value;
	}
	
	public ProvinceRequest(Province province) {
		this.province_id = province.getProvince_id();
		this.province_value = province.getProvince_value();
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public String getProvince_value() {
		return province_value;
	}

	public void setProvince_value(String province_value) {
		this.province_value = province_value;
	}
}
