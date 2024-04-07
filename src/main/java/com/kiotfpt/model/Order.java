package com.kiotfpt.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kiotfpt_order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int order_id;

	@Column(name = "order_time_init", nullable = false)
	private Date order_time_init;

	@Column(name = "order_time_complete")
	private Date order_time_complete;

	@Column(name = "order_desc", nullable = false)
	private String order_desc;

	@Column(name = "order_total", nullable = false)
	private float order_total;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "section_id", referencedColumnName = "section_id")
	private Section section;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	public Order() {
		super();
	}

	public Order(int order_id, Date order_time_init, Date order_time_complete,
			String order_desc, float order_total, Section section, Shop shop, Account account,
			Status status) {
		super();
		this.order_id = order_id;
		this.order_time_init = order_time_init;
		this.order_time_complete = order_time_complete;
		this.order_desc = order_desc;
		this.order_total = order_total;
		this.section = section;
		this.shop = shop;
		this.account = account;
		this.status = status;
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

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
