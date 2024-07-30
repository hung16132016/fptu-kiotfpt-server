package com.kiotfpt.response;

import com.kiotfpt.model.Province;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {

	private int province_id;
	private String province_value;

	public ProvinceResponse(Province province) {
		this.province_id = province.getId();
		this.province_value = province.getValue();
	}

}
