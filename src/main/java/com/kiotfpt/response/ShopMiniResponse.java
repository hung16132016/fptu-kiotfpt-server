package com.kiotfpt.response;

import com.kiotfpt.model.Shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopMiniResponse {

	private int id;
	private String name;
	private String thumbnail;

	public ShopMiniResponse(Shop shop) {
		super();
		this.id = shop.getId();
		this.name = shop.getName();
		this.thumbnail = shop.getThumbnail();
	}

}
