package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {

	private int id;
	private String name;
	private String email;
	private String phone;
	private String thumbnail;
	private float rate;
	private boolean official;
	private int follower;
	private int address_id;
	private int account_id;

}
