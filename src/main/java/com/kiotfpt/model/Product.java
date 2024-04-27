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

@Entity
@Table(name = "kiotfpt_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int product_id;

	@Column(name = "product_name", nullable = false)
	private String product_name;

	@Column(name = "product_description", nullable = false)
	private String product_description;
	
	@Column(name = "product_sold", nullable = false)
	private int product_sold;

	@Column(name = "product_price", nullable = false)
	private float product_price;

	@Column(name = "product_best_seller", nullable = false)
	private boolean product_best_seller;

	@Column(name = "product_popular", nullable = false)
	private boolean product_popular;

	@Column(name = "product_variants", nullable = false)
	private String product_variants;
	
	@Column(name = "product_repository", nullable = false)
	private int product_repository;
	
	@ManyToOne()
	@JoinColumn(name = "pc_id")
	private Product_Condition product_condition;
	
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
	private Collection<Accessibility_item> items;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Product_Thumbnail> thumbnail;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Comment> comments;

	public Product() {
		super();
	}

	public Product(int product_id, String product_name, String product_description, int product_sold,
			float product_price, boolean product_best_seller, boolean product_popular, String product_variants,
			int product_repository, Status status, Category category, Shop shop, Collection<Accessibility_item> items,
			List<Product_Thumbnail> thumbnail, Collection<Comment> comments) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_description = product_description;
		this.product_sold = product_sold;
		this.product_price = product_price;
		this.product_best_seller = product_best_seller;
		this.product_popular = product_popular;
		this.product_variants = product_variants;
		this.product_repository = product_repository;
		this.status = status;
		this.category = category;
		this.shop = shop;
		this.items = items;
		this.thumbnail = thumbnail;
		this.comments = comments;
	}



	public int getProduct_id() {
		return product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public float getProduct_price() {
		return product_price;
	}

	public void setProduct_price(float product_price) {
		this.product_price = product_price;
	}

	public boolean isProduct_best_seller() {
		return product_best_seller;
	}

	public void setProduct_best_seller(boolean product_best_seller) {
		this.product_best_seller = product_best_seller;
	}

	public boolean isProduct_popular() {
		return product_popular;
	}

	public void setProduct_popular(boolean product_popular) {
		this.product_popular = product_popular;
	}

	public String getProduct_variants() {
		return product_variants;
	}

	public void setProduct_variants(String product_variants) {
		this.product_variants = product_variants;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Collection<Accessibility_item> getItems() {
		return items;
	}

	public void setItems(Collection<Accessibility_item> items) {
		this.items = items;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Product_Thumbnail> getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(List<Product_Thumbnail> thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getProduct_sold() {
		return product_sold;
	}

	public void setProduct_sold(int product_sold) {
		this.product_sold = product_sold;
	}

	public int getProduct_repository() {
		return product_repository;
	}

	public void setProduct_repository(int product_repository) {
		this.product_repository = product_repository;
	}

	public Product_Condition getProduct_condition() {
		return product_condition;
	}

	public void setProduct_condition(Product_Condition product_condition) {
		this.product_condition = product_condition;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
