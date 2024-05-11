package com.kiotfpt.response;

import java.util.Date;

import com.kiotfpt.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private int order_id;
	private Date order_time_init;
	private Date order_time_complete;
	private String order_desc;
	private float order_total;
	private StatusResponse status;

	public OrderResponse(Order order) {
		super();
		this.order_id = order.getId();
		this.order_time_init = order.getTimeInit();
		this.order_time_complete = order.getTimeComplete();
		this.order_desc = order.getDesc();
		this.order_total = order.getTotal();
		this.status = new StatusResponse(order.getStatus());
	}

}
