package com.kiotfpt.response;

import com.kiotfpt.model.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

	private int address_id;
	private String address_value;
	private DistrictResponse district;
	private ProvinceResponse province;
	private boolean isdefault;

	public AddressResponse(Address address) {
		super();
		this.address_id = address.getId();
		this.address_value = address.getValue();
		this.district = new DistrictResponse(address.getDistrict());
		this.province = new ProvinceResponse(address.getProvince());
		this.isdefault = address.isIsdefault();
	}

}
