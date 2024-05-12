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
@Table(name = "kiotfpt_section")
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "section_id")
	private int id;

	@Column(name = "section_total", nullable = false)
	private float total;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;
	
	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne()
	@JsonIgnore
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private Collection<AccessibilityItem> items;

}
