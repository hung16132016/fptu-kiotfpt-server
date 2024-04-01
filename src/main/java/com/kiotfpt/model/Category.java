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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int category_id;

	@Column(name = "category_name", nullable = false)
	private String name;

	@Column(name = "category_thumbnail", nullable = false)
	private String category_thumbnail;

	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;

	public Category() {
		super();
	}

	public Category(String category_name, String category_thumbnail, Status status) {
		super();
		this.name = category_name;
		this.category_thumbnail = category_thumbnail;
		this.status = status;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return name;
	}

	public void setCategory_name(String category_name) {
		this.name = category_name;
	}

	public String getCategory_thumbnail() {
		return category_thumbnail;
	}

	public void setCategory_thumbnail(String category_thumbnail) {
		this.category_thumbnail = category_thumbnail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
