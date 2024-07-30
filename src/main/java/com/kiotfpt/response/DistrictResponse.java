package com.kiotfpt.response;

import com.kiotfpt.model.District;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse {

	private int district_id;
	private String district_value;

	public DistrictResponse(District district) {
		this.district_id = district.getId();
		this.district_value = district.getValue();
	}

}
