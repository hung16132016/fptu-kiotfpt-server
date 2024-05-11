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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_status")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id", nullable = false)
	private int id;

	@Column(name = "status_value", nullable = false)
	private String value;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Account> accounts;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<AccessibilityItem> items;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Category> category;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Section> sections;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Order> orders;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Voucher> vouchers;
}
