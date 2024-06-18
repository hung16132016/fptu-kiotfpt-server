package com.kiotfpt.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

	private int section_id;
	private float section_total;
	private ShopMiniResponse shop;
	private StatusResponse status;
	private List<Accessibility_itemResponse> items;

	
}
