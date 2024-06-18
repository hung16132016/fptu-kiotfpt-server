package com.kiotfpt.response;

import com.kiotfpt.model.AccessibilityItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {

	private int id;
	private String name;
	private String thumbnail;
	private int amount;
	private VariantResponse variant;

	public ProductOrderResponse(AccessibilityItem item) {
		this.id = item.getVariant().getProduct().getId();
		this.name = item.getVariant().getProduct().getName();
		this.thumbnail = item.getVariant().getProduct().getThumbnail().get(0).getLink();
		this.amount = item.getQuantity();
		this.variant = new VariantResponse(item.getVariant(), item);
	}
	
}
