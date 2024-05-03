package com.kiotfpt.request;

import com.kiotfpt.model.Shop;

public class ShopRequest {

	private int shop_id;
	private String shop_name;
	private String shop_email;
	private String shop_phone;
	private String shop_thumbnail;

	private AddressRequest address;

	public ShopRequest() {
	}

	public ShopRequest(int shop_id, String shop_name, String shop_email, String shop_phone, String shop_thumbnail,
			AddressRequest address) {
		this.shop_id = shop_id;
		this.shop_name = shop_name;
		this.shop_email = shop_email;
		this.shop_phone = shop_phone;
		this.shop_thumbnail = shop_thumbnail;
		this.address = address;
	}
	
    public ShopRequest(Shop shop) {
    	super();
        this.shop_id = shop.getShop_id();
        this.shop_name = shop.getShop_name();
        this.shop_email = shop.getShop_email();
        this.shop_phone = shop.getShop_phone();
        this.shop_thumbnail = shop.getShop_thumbnail();
        this.address = new AddressRequest(shop.getAddress());
    }

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_email() {
		return shop_email;
	}

	public void setShop_email(String shop_email) {
		this.shop_email = shop_email;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getShop_thumbnail() {
		return shop_thumbnail;
	}

	public void setShop_thumbnail(String shop_thumbnail) {
		this.shop_thumbnail = shop_thumbnail;
	}

	public AddressRequest getAddress() {
		return address;
	}

	public void setAddress(AddressRequest address) {
		this.address = address;
	}
}
