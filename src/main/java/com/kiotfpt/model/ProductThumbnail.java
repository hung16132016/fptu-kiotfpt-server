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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_product_thumbnail")
public class ProductThumbnail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "thumbnail_id")
	private int id;

	@Column(name = "thumbnail_link", nullable = false)
	private String link;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

}
