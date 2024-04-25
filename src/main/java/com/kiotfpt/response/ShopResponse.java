package com.kiotfpt.response;

public class ShopResponse {

	private int shop_id;
	private String shop_name;
	private String shop_email;
	private String shop_phone;
	private String shop_thumbnail;

	private AddressResponse address;

	public ShopResponse() {
	}

	public ShopResponse(int shop_id, String shop_name, String shop_email, String shop_phone, String shop_thumbnail,
			AddressResponse address) {
		this.shop_id = shop_id;
		this.shop_name = shop_name;
		this.shop_email = shop_email;
		this.shop_phone = shop_phone;
		this.shop_thumbnail = shop_thumbnail;
		this.address = address;
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

	public AddressResponse getAddress() {
		return address;
	}

	public void setAddress(AddressResponse address) {
		this.address = address;
	}
}
