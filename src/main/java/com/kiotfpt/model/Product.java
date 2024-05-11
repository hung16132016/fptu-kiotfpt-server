package com.kiotfpt.model;

import java.util.Collection;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int  id;

	@Column(name = "product_sold")
	private int  sold;

	@Column(name = "product_discount")
	private int  discount;

	@Column(name = "product_name")
	private String  name;

	@Column(name = "product_description")
	private String  description;

	@Column(name = "product_min_price")
	private float  minPrice;

	@Column(name = "product_max_price")
	private float  maxPrice;

	@Column(name = "product_rate")
	private float  rate;

	@Column(name = "product_best_seller")
	private boolean  bestSeller;

	@Column(name = "product_popular")
	private boolean  popular;

	@Column(name = "product_top_deal")
	private boolean  topDeal;

	@Column(name = "product_official")
	private boolean  official;

	@ManyToOne()
	@JoinColumn(name = "pc_id")
	private ProductCondition  condition;

	@ManyToOne()
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "shop_id")
	@JsonIgnore
	private Shop shop;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<AccessibilityItem> items;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductThumbnail> thumbnail;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Comment> comments;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<ProductFavourite> favourite;
}
