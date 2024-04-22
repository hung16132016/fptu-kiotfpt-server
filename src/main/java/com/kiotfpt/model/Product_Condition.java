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

@Entity
@Table(name = "kiotfpt_product_condition")
public class Product_Condition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pc_id;

	@Column(name = "pc_value", nullable = false)
	private String pc_value;
	
	@OneToMany(mappedBy = "product_condition", cascade = CascadeType.ALL)
	private Collection<Product> products;

	public Product_Condition() {
		super();
	}

	public Product_Condition(int pc_id, String pc_value, Collection<Product> products) {
		super();
		this.pc_id = pc_id;
		this.pc_value = pc_value;
		this.products = products;
	}

	public int getPc_id() {
		return pc_id;
	}

	public void setPc_id(int pc_id) {
		this.pc_id = pc_id;
	}

	public String getPc_value() {
		return pc_value;
	}

	public void setPc_value(String pc_value) {
		this.pc_value = pc_value;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}
	
	
}
