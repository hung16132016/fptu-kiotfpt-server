package com.kiotfpt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_accessibility_item")
public class Accessibility_item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int item_id;

	@Column(name = "item_quantity", nullable = false)
	private int item_quantity;

	@Column(name = "item_total", nullable = false)
	private double item_total;

	@Column(name = "item_note", nullable = false)
	private int item_note;

	@ManyToOne
	@JoinColumn(name = "section_id")
	@JsonIgnore
	private Section section;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

	@ManyToOne()
	@JoinColumn(name = "status_id")
	private Status status;

	public Accessibility_item() {
		super();
	}

	public Accessibility_item(int item_quantity, double item_total, int item_note, Status status) {
		super();
		this.item_quantity = item_quantity;
		this.item_total = item_total;
		this.item_note = item_note;
		this.status = status;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getItem_quantity() {
		return item_quantity;
	}

	public void setItem_quantity(int item_quantity) {
		this.item_quantity = item_quantity;
	}

	public double getItem_total() {
		return item_total;
	}

	public void setItem_total(double item_total) {
		this.item_total = item_total;
	}

	public int getItem_note() {
		return item_note;
	}

	public void setItem_note(int item_note) {
		this.item_note = item_note;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
