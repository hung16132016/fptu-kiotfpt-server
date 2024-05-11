package com.kiotfpt.request;

import com.kiotfpt.model.District;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictRequest {

	private int district_id;
	private String district_value;

	public DistrictRequest(District district) {
		this.district_id = district.getId();
		this.district_value = district.getValue();
	}

}
