package com.kiotfpt.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompositeKey_LineItem implements Serializable {

	private int productId;
	private int orderId;

	public CompositeKey_LineItem() {
	}

	public CompositeKey_LineItem(int productId, int order_id) {
		super();
		this.productId = productId;
		this.orderId = order_id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrder_id(int order_id) {
		this.orderId = order_id;
	}

}
