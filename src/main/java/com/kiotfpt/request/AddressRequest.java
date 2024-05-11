package com.kiotfpt.request;

import com.kiotfpt.model.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

	private int address_id;
	private String address_value;
	private DistrictRequest district;
	private ProvinceRequest province;

	public AddressRequest(Address address) {
		super();
		this.address_id = address.getId();
		this.address_value = address.getValue();
		this.district = new DistrictRequest(address.getDistrict());
		this.province = new ProvinceRequest(address.getProvince());
	}

}
