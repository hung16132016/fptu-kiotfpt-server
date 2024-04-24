package com.kiotfpt.response;

public class ProvinceResponse {

	private int province_id;
	private String province_value;

	public ProvinceResponse() {
	}

	public ProvinceResponse(int province_id, String province_value) {
		this.province_id = province_id;
		this.province_value = province_value;
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
