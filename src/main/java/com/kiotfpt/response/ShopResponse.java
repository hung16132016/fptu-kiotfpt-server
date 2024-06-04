package com.kiotfpt.response;

import java.util.ArrayList;
import java.util.List;

import com.kiotfpt.model.Product;
import com.kiotfpt.model.Shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {

	private int id;
	private String name;
	private String email;
	private String phone;
	private String thumbnail;
	private float rate;
	private boolean official;
	private int follower;
	private AddressResponse address;
	private List<ProductMiniResponse> product;

	public ShopResponse(Shop shop) {
		super();
		this.id = shop.getId();
		this.name = shop.getName();
		this.email = shop.getEmail();
		this.phone = shop.getPhone();
		this.thumbnail = shop.getThumbnail();
		this.address = new AddressResponse(shop.getAddress());
	}

	public ShopResponse(Shop shop, List<Product> products) {
		super();
		this.id = shop.getId();
		this.name = shop.getName();
		this.email = shop.getEmail();
		this.phone = shop.getPhone();
		this.thumbnail = shop.getThumbnail();
		this.address = new AddressResponse(shop.getAddress());
		List<ProductMiniResponse> list = new ArrayList<>();
		for (Product pro : products) {
			list.add(new ProductMiniResponse(pro, shop));
		}
		this.product = list;
	}
}
