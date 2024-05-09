package com.kiotfpt.model;

import java.util.Collection;

import javax.persistence.CascadeType;
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
@Table(name = "kiotfpt_cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cart_id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnore
	private Account account;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private Collection<Section> sections;

	public Cart() {
		super();
	}

	public Cart(Account account, Collection<Section> sections) {
		super();
		this.account = account;
		this.sections = sections;
	}

	public int getCart_id() {
		return cart_id;
	}

	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}

	public Collection<Section> getSections() {
		return sections;
	}

	public void setSections(Collection<Section> sections) {
		this.sections = sections;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
