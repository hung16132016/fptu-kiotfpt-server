package com.kiotfpt.response;

import java.util.List;

public class SectionResponse {

	private int section_id;

	private float section_total;

	private ShopResponse shop;

	private StatusResponse status;

	private List<Accessibility_itemResponse> items;

	public SectionResponse() {
		super();
	}

	public SectionResponse(int section_id, float section_total, ShopResponse shop, StatusResponse status,
			List<Accessibility_itemResponse> items) {
		super();
		this.section_id = section_id;
		this.section_total = section_total;
		this.shop = shop;
		this.status = status;
		this.items = items;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public float getSection_total() {
		return section_total;
	}

	public void setSection_total(float section_total) {
		this.section_total = section_total;
	}

	public ShopResponse getShop() {
		return shop;
	}

	public void setShop(ShopResponse shop) {
		this.shop = shop;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public List<Accessibility_itemResponse> getItems() {
		return items;
	}

	public void setItems(List<Accessibility_itemResponse> items) {
		this.items = items;
	}
	
}
