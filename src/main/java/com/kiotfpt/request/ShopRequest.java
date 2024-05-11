package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {

	private int shop_id;
	private String shop_name;
	private String shop_email;
	private String shop_phone;
	private String shop_thumbnail;
	private AddressRequest address;

//    public ShopRequest(Shop shop) {
//    	super();
//        this.shop_id = shop.getShop_id();
//        this.shop_name = shop.getShop_name();
//        this.shop_email = shop.getShop_email();
//        this.shop_phone = shop.getShop_phone();
//        this.shop_thumbnail = shop.getShop_thumbnail();
//        this.address = new AddressRequest(shop.getAddress());
//    }

}
