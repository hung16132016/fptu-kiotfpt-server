package com.kiotfpt.response;

import java.util.ArrayList;
import java.util.List;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDesc {
	
	private float section_total;
	private List<Accessibility_itemResponse> items;
	
	public TransactionDesc(Order order) {
		super();
		this.section_total = order.getTotal();
		
		List<Accessibility_itemResponse> list = new ArrayList<>();
		for (AccessibilityItem item : order.getSection().getItems()) {
			list.add(new Accessibility_itemResponse(item, item.getVariant().getProduct()));
		}
		this.items = list;
	}
	
}
