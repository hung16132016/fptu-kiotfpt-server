package com.kiotfpt.request;

import com.kiotfpt.model.Address;

public class AddressRequest {

	private int address_id;
	private String address_value;
	private DistrictRequest district;
	private ProvinceRequest province;

	public AddressRequest() {
	}

	public AddressRequest(int address_id, String address_value, DistrictRequest district, ProvinceRequest province) {
		this.address_id = address_id;
		this.address_value = address_value;
		this.district = district;
		this.province = province;
	}
	
	public AddressRequest(Address address) {
		super();
		this.address_id = address.getAddress_id();
		this.address_value = address.getAddress_value();
		this.district = new DistrictRequest(address.getDistrict());
		this.province = new ProvinceRequest(address.getProvince());
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String getAddress_value() {
		return address_value;
	}

	public void setAddress_value(String address_value) {
		this.address_value = address_value;
	}

	public DistrictRequest getDistrict() {
		return district;
	}

	public void setDistrict(DistrictRequest district) {
		this.district = district;
	}

	public ProvinceRequest getProvince() {
		return province;
	}

	public void setProvince(ProvinceRequest province) {
		this.province = province;
	}
}
