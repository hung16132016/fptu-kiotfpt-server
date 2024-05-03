package com.kiotfpt.request;

public class ItemRequest {

	private int item_id;
	private int item_quantity;
	private double item_total;
	private String item_note;

	public int getItemId() {
		return item_id;
	}

	public ItemRequest() {
		super();
	}

	public ItemRequest(int itemId, int itemQuantity, double itemTotal, String itemNote) {
		super();
		this.item_id = itemId;
		this.item_quantity = itemQuantity;
		this.item_total = itemTotal;
		this.item_note = itemNote;
	}

	public void setItemId(int itemId) {
		this.item_id = itemId;
	}

	public int getItemQuantity() {
		return item_quantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.item_quantity = itemQuantity;
	}

	public double getItemTotal() {
		return item_total;
	}

	public void setItemTotal(double itemTotal) {
		this.item_total = itemTotal;
	}

	public String getItemNote() {
		return item_note;
	}

	public void setItemNote(String itemNote) {
		this.item_note = itemNote;
	}
}
