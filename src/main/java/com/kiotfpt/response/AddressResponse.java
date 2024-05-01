package com.kiotfpt.response;

import com.kiotfpt.model.Address;
import com.kiotfpt.request.AddressRequest;

public class AddressResponse {

	private int address_id;
	private String address_value;
	private DistrictResponse district;
	private ProvinceResponse province;

	public AddressResponse() {
	}

	public AddressResponse(int address_id, String address_value, DistrictResponse district, ProvinceResponse province) {
		super();
		this.address_id = address_id;
		this.address_value = address_value;
		this.district = district;
		this.province = province;
	}
	
	public AddressResponse(AddressRequest address) {
		super();
		this.address_id = address.getAddress_id();
		this.address_value = address.getAddress_value();
		this.district = new DistrictResponse(address.getDistrict());
		this.province = new ProvinceResponse(address.getProvince());
	}
	
	public AddressResponse(Address address) {
		super();
		this.address_id = address.getAddress_id();
		this.address_value = address.getAddress_value();
		this.district = new DistrictResponse(address.getDistrict());
		this.province = new ProvinceResponse(address.getProvince());
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

	public DistrictResponse getDistrict() {
		return district;
	}

	public void setDistrict(DistrictResponse district) {
		this.district = district;
	}

	public ProvinceResponse getProvince() {
		return province;
	}

	public void setProvince(ProvinceResponse province) {
		this.province = province;
	}
}
