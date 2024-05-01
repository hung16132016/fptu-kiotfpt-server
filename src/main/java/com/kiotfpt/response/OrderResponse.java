package com.kiotfpt.response;

import java.util.Date;

import com.kiotfpt.model.Order;

public class OrderResponse {
	
    private int order_id;
    private Date order_time_init;
    private Date order_time_complete;
    private String order_desc;
    private float order_total;
    private StatusResponse status;
    
	public OrderResponse() {
		super();
	}

	public OrderResponse(int order_id, Date order_time_init, Date order_time_complete, String order_desc,
			float order_total, StatusResponse status) {
		super();
		this.order_id = order_id;
		this.order_time_init = order_time_init;
		this.order_time_complete = order_time_complete;
		this.order_desc = order_desc;
		this.order_total = order_total;
		this.status = status;
	}
	
	public OrderResponse(Order order) {
		super();
		this.order_id = order.getOrder_id();
		this.order_time_init = order.getOrder_time_init();
		this.order_time_complete = order.getOrder_time_complete();
		this.order_desc = order.getOrder_desc();
		this.order_total = order.getOrder_total();
		this.status = new StatusResponse(order.getStatus());
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public Date getOrder_time_init() {
		return order_time_init;
	}

	public void setOrder_time_init(Date order_time_init) {
		this.order_time_init = order_time_init;
	}

	public Date getOrder_time_complete() {
		return order_time_complete;
	}

	public void setOrder_time_complete(Date order_time_complete) {
		this.order_time_complete = order_time_complete;
	}

	public String getOrder_desc() {
		return order_desc;
	}

	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}

	public float getOrder_total() {
		return order_total;
	}

	public void setOrder_total(float order_total) {
		this.order_total = order_total;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}
}
