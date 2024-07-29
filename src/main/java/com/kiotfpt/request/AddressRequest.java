package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

	private int address_id;
	private int account_id;
	private String address_value;
	private int district_id;
	private int province_id;
	private boolean isDefault;
	
}
