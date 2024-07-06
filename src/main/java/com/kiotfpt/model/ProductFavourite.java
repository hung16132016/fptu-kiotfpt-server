package com.kiotfpt.model;

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
@Table(name = "kiotfpt_product_favourite")
public class ProductFavourite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "product_favourite_id")
	private int id;

	@ManyToOne()
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private Account account;

	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;
}
