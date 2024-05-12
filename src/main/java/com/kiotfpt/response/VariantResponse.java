package com.kiotfpt.response;

import com.kiotfpt.model.Variant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponse {

	private int id;
	private int price;
	private int quantity;
	private ColorResponse color;
	private SizeResponse size;
	private ProductResponse product;
	

	public VariantResponse(Variant repo) {
		super();
		this.id = repo.getId();
		this.price = repo.getPrice();
		this.quantity = repo.getQuantity();
		this.color = new ColorResponse(repo.getColor());
		this.size = new SizeResponse(repo.getSize());
		this.product = new ProductResponse(repo.getProduct());
	}

}
