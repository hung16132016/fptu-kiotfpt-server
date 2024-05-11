package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

	private int item_id;
	private int item_quantity;
	private double item_total;
	private String item_note;

}
