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
import com.kiotfpt.request.ShopRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_shop")
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_id")
	private int id;

	@Column(name = "shop_name")
	private String name;

	@Column(name = "shop_email")
	private String email;

	@Column(name = "shop_phone")
	private String phone;

	@Column(name = "shop_thumbnail")
	private String thumbnail;
	
	@Column(name = "shop_rate")
	private float rate;
	
	@Column(name = "shop_official")
	private boolean official;
	
	@Column(name = "shop_follower")
	private int follower;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnore
	private Account account;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address address;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Order> orders;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> sections;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Transaction> transactions;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Follow> follows;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Voucher> voucher;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<ShopCategory> shopcategories;

	public Shop(ShopRequest request, Account account, Address address) {
		super();
		this.name = request.getName();
		this.email = request.getEmail();
		this.phone = request.getPhone();
		this.thumbnail = request.getThumbnail();
		this.rate = 0;
		this.official = false;
		this.follower = 0;
		this.account = account;
		this.address = address;
	}
	
	
}
