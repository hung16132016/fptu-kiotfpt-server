package com.kiotfpt.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private int id;
	private LocalDateTime time_init;
	private LocalDateTime time_complete;
	private String desc;
	private float total;
	private int voucherValue;
	private ShopMiniResponse shop;
	private List<ProductOrderResponse> product;
	private StatusResponse status;
	private ProfileMiniResponse profile;

	public OrderResponse(Order order) {
		super();
		this.id = order.getId();
		this.time_init = order.getTimeInit();
		this.time_complete = order.getTimeComplete();
		this.desc = order.getDesc();
		this.total = order.getTotal();
		this.shop = new ShopMiniResponse(order.getShop());

		Collection<AccessibilityItem> items = order.getSection().getItems();
		List<ProductOrderResponse> list = new ArrayList<ProductOrderResponse>();
		for (AccessibilityItem item : items) {
			list.add(new ProductOrderResponse(item));
		}

		this.product = list;
		this.status = new StatusResponse(order.getStatus());
	}

	public OrderResponse(Order order, AccountProfile profile) {
		super();
		this.id = order.getId();
		this.time_init = order.getTimeInit();
		this.time_complete = order.getTimeComplete();
		this.desc = order.getDesc();
		this.total = order.getTotal();
		this.shop = new ShopMiniResponse(order.getShop());

		Collection<AccessibilityItem> items = order.getSection().getItems();
		List<ProductOrderResponse> list = new ArrayList<>();
		float totalItemTotal = 0;
		for (AccessibilityItem item : items) {
			list.add(new ProductOrderResponse(item));
			totalItemTotal += item.getTotal();
		}

		this.product = list;
		this.status = new StatusResponse(order.getStatus());
		this.profile = new ProfileMiniResponse(profile);
		if (order.getTotal() == totalItemTotal) {
			this.voucherValue = 0;
		} else {
			this.voucherValue = totalItemTotal != 0 ? (int) (100 - (order.getTotal() / totalItemTotal) * 100) : 0;
		}
	}
}
