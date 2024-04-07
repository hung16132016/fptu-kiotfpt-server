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
	private Collection<Accessibility_item> items;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Category> category;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> sections;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Order> orders;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Collection<Accessibility_item> getItems() {
		return items;
	}

	public void setItems(Collection<Accessibility_item> items) {
		this.items = items;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	public Collection<Section> getSections() {
		return sections;
	}

	public void setSections(Collection<Section> sections) {
		this.sections = sections;
	}

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

}
