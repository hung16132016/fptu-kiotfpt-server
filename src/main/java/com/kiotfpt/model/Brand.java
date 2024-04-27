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
@Table(name = "kiotfpt_brand")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int brand_id;
	
	@Column(name = "brand_name", nullable = false)
	private String brand_name;
	
	@Column(name = "brand_thumbnail", nullable = false)
	private String brand_thumbnail;
	
	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;

	public Brand() {
		super();
	}

	public Brand(int brand_id, String brand_name, String brand_thumbnail) {
		super();
		this.brand_id = brand_id;
		this.brand_name = brand_name;
		this.brand_thumbnail = brand_thumbnail;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBrand_thumbnail() {
		return brand_thumbnail;
	}

	public void setBrand_thumbnail(String brand_thumbnail) {
		this.brand_thumbnail = brand_thumbnail;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}
	
	
}
