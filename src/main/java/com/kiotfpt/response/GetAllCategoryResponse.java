package com.kiotfpt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCategoryResponse {

	private int id;
	private String name;
	private String thumbnail;
	private StatusResponse status;
	private int product_total;
}
