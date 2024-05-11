package com.kiotfpt.response;

import com.kiotfpt.model.Shop;
import com.kiotfpt.request.ShopRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {

	private int shop_id;
	private String shop_name;
	private String shop_email;
	private String shop_phone;
	private String shop_thumbnail;

	private AddressResponse address;
	
    public ShopResponse(ShopRequest shopRequest) {
    	super();
        this.shop_id = shopRequest.getShop_id();
        this.shop_name = shopRequest.getShop_name();
        this.shop_email = shopRequest.getShop_email();
        this.shop_phone = shopRequest.getShop_phone();
        this.shop_thumbnail = shopRequest.getShop_thumbnail();
        this.address = new AddressResponse(shopRequest.getAddress());
    }
    
    public ShopResponse(Shop shop) {
    	super();
        this.shop_id = shop.getId();
        this.shop_name = shop.getName();
        this.shop_email = shop.getEmail();
        this.shop_phone = shop.getPhone();
        this.shop_thumbnail = shop.getThumbnail();
        this.address = new AddressResponse(shop.getAddress());
    }
}
