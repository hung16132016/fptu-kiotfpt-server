package com.kiotfpt.request;

import com.kiotfpt.model.Shop;

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
	private AddressRequest address;
	private AccountRequest account;

    public ShopRequest(Shop shop) {
    	super();
        this.id = shop.getId();
        this.name = shop.getName();
        this.email = shop.getEmail();
        this.phone = shop.getPhone();
        this.thumbnail = shop.getThumbnail();
        this.rate = shop.getRate();
        this.official = shop.isOfficial();
        this.follower = shop.getFollower();
        this.address = new AddressRequest(shop.getAddress());
    }

}
