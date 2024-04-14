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

	@Column(name = "product_price", nullable = false)
	private float product_price;

	@Column(name = "product_best_seller", nullable = false)
	private boolean product_best_seller;

	@Column(name = "product_popular", nullable = false)
	private boolean product_popular;

	@Column(name = "product_variants", nullable = false)
	private String product_variants;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Collection<Accessibility_item> items;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Product_Thumbnail> thumbnail;

	public Product() {
		super();
	}

	public Product(String product_name, String product_description, float product_price,
			boolean product_best_seller, boolean product_popular, String product_variants, Status status,
			Category category, List<Product_Thumbnail> thumbnail) {
		super();
		this.product_name = product_name;
		this.product_description = product_description;
		this.product_price = product_price;
		this.product_best_seller = product_best_seller;
		this.product_popular = product_popular;
		this.product_variants = product_variants;
		this.status = status;
		this.category = category;
		this.thumbnail = thumbnail;
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

}
