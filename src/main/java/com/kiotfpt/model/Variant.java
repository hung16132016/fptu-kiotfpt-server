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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_variant")
public class Variant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "variant_id")
	private int id;

	@Column(name = "product_price")
	private int price;
	
	@Column(name = "product_quantity")
	private int quantity;
	
	@ManyToOne()
	@JoinColumn(name = "color_id", nullable = false)
	private Color color;
	
	@ManyToOne()
	@JoinColumn(name = "size_id", nullable = false)
	private Size size;
	
	@ManyToOne()
	@JoinColumn(name = "size_id", nullable = false)
	private Product product;
	
	@OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<AccessibilityItem> items;

}
