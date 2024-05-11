package com.kiotfpt.request;

import com.kiotfpt.model.Province;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceRequest {

	private int province_id;
	private String province_value;

	public ProvinceRequest(Province province) {
		this.province_id = province.getId();
		this.province_value = province.getValue();
	}

}
