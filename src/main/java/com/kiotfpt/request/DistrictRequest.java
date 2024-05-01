package com.kiotfpt.request;

import com.kiotfpt.model.District;

public class DistrictRequest {

	private int district_id;
	private String district_value;

	public DistrictRequest() {
	}

	public DistrictRequest(int district_id, String district_value) {
		this.district_id = district_id;
		this.district_value = district_value;
	}

	public DistrictRequest(District district) {
		this.district_id = district.getDistrict_id();
		this.district_value = district.getDistrict_value();
	}
	
	public int getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}

	public String getDistrict_value() {
		return district_value;
	}

	public void setDistrict_value(String district_value) {
		this.district_value = district_value;
	}
}
