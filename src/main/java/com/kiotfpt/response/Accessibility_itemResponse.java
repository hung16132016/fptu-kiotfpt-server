package com.kiotfpt.response;

import com.kiotfpt.model.Accessibility_item;

public class Accessibility_itemResponse {


	private int item_id;

	private int item_quantity;

	private double item_total;

	private String item_note;

	private ProductResponse product;

	private StatusResponse status;

	public Accessibility_itemResponse() {
		super();
	}

	public Accessibility_itemResponse(int item_id, int item_quantity, double item_total, String item_note,
			ProductResponse product, StatusResponse status) {
		super();
		this.item_id = item_id;
		this.item_quantity = item_quantity;
		this.item_total = item_total;
		this.item_note = item_note;
		this.product = product;
		this.status = status;
	}
	
	public Accessibility_itemResponse(Accessibility_item item) {
		super();
		this.item_id = item.getItem_id();
		this.item_quantity = item.getItem_quantity();
		this.item_total = item.getItem_total();
		this.item_note = item.getItem_note();
		this.product = new ProductResponse(item.getProduct());
		this.status = new StatusResponse(item.getStatus());
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getItem_quantity() {
		return item_quantity;
	}

	public void setItem_quantity(int item_quantity) {
		this.item_quantity = item_quantity;
	}

	public double getItem_total() {
		return item_total;
	}

	public void setItem_total(double item_total) {
		this.item_total = item_total;
	}

	public String getItem_note() {
		return item_note;
	}

	public void setItem_note(String item_note) {
		this.item_note = item_note;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public void setProduct(ProductResponse product) {
		this.product = product;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}
	
}
