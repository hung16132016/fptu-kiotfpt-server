package com.kiotfpt.response;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accessibility_itemResponse {

	private int id;
	private int quantity;
	private double total;
	private String note;
	private VariantResponse repo;
	private StatusResponse status;
	private ProductResponse product;

	public Accessibility_itemResponse(AccessibilityItem item) {
		super();
		this.id = item.getId();
		this.quantity = item.getQuantity();
		this.total = item.getTotal();
		this.note = item.getNote();
		this.repo = new VariantResponse(item.getVariant());
		this.status = new StatusResponse(item.getStatus());
	}
	
	public Accessibility_itemResponse(AccessibilityItem item, Product product) {
		super();
		this.id = item.getId();
		this.quantity = item.getQuantity();
		this.total = item.getTotal();
		this.note = item.getNote();
		this.repo = new VariantResponse(item.getVariant());
		this.status = new StatusResponse(item.getStatus());
		this.product = new ProductResponse(product, item);
	}

}
