package com.kiotfpt.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "kiotfpt_section")
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int section_id;

	@Column(name = "section_total", nullable = false)
	private float section_total;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;
	
	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne()
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private Collection<Accessibility_item> items;

	public Section() {
		super();
	}

	public Section(float section_total, Shop shop, Status status) {
		super();
		this.section_total = section_total;
		this.shop = shop;
		this.status = status;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public float getSection_total() {
		return section_total;
	}

	public void setSection_total(float section_total) {
		this.section_total = section_total;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Collection<Accessibility_item> getItems() {
		return items;
	}

	public void setItems(Collection<Accessibility_item> items) {
		this.items = items;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

}
