package com.kiotfpt.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_status")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int status_id;
	
	@Column(name = "status_value", nullable = false)
	private String value;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Account> accounts;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Accessibility_item> sub_orders;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Category> category;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> orders;

	public Status() {
		super();
	}

	public Status(String status_value) {
		super();
		this.value = status_value;
	}

	public int getStatus_id() {
		return status_id;
	}

	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	public String getStatus_value() {
		return value;
	}

	public void setStatus_value(String status_value) {
		this.value = status_value;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}

	public Collection<Accessibility_item> getSub_orders() {
		return sub_orders;
	}

	public void setSub_orders(Collection<Accessibility_item> sub_orders) {
		this.sub_orders = sub_orders;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	public Collection<Section> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Section> orders) {
		this.orders = orders;
	}
}
