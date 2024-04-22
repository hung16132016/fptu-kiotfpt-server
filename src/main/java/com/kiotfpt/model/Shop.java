package com.kiotfpt.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_shop")
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int shop_id;

	@Column(name = "shop_name", nullable = false)
	private String shop_name;

	@Column(name = "shop_email", nullable = false)
	private String shop_email;

	@Column(name = "shop_phone", nullable = false)
	private String shop_phone;

	@Column(name = "shop_thumbnail", nullable = false)
	private String shop_thumbnail;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnore
	private Account account;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address address;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> orders;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> sections;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Transaction> transactions;

	public Shop() {
		super();
	}

	public Shop(String shop_name, String shop_email, String shop_phone,
			String shop_thumbnail, Account account, Address address) {
		super();
		this.shop_name = shop_name;
		this.shop_email = shop_email;
		this.shop_phone = shop_phone;
		this.shop_thumbnail = shop_thumbnail;
		this.account = account;
		this.address = address;
	}

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_email() {
		return shop_email;
	}

	public void setShop_email(String shop_email) {
		this.shop_email = shop_email;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getShop_thumbnail() {
		return shop_thumbnail;
	}

	public void setShop_thumbnail(String shop_thumbnail) {
		this.shop_thumbnail = shop_thumbnail;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Collection<Section> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Section> orders) {
		this.orders = orders;
	}

	public Collection<Section> getSections() {
		return sections;
	}

	public void setSections(Collection<Section> sections) {
		this.sections = sections;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	public Collection<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}
	
}
