package com.kiotfpt.response;

public class DistrictResponse {

	private int district_id;
	private String district_value;

	public DistrictResponse() {
	}

	public DistrictResponse(int district_id, String district_value) {
		this.district_id = district_id;
		this.district_value = district_value;
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
