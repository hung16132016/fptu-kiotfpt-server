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
@Table(name = "kiotfpt_product_thumbnail")
public class Product_Thumbnail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int thumbnail_id;

	@Column(name = "thumbnail_link", nullable = false)
	private String thumbnail_link;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

	public Product_Thumbnail() {
		super();
	}

	public Product_Thumbnail(String thumbnail_link) {
		super();
		this.thumbnail_link = thumbnail_link;
	}

	public int getThumbnail_id() {
		return thumbnail_id;
	}

	public void setThumbnail_id(int thumbnail_id) {
		this.thumbnail_id = thumbnail_id;
	}

	public String getThumbnail_link() {
		return thumbnail_link;
	}

	public void setThumbnail_link(String thumbnail_link) {
		this.thumbnail_link = thumbnail_link;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
