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
@Table(name = "kiotfpt_brand")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private int id;
	
	@Column(name = "brand_name", nullable = false)
	private String name;
	
	@Column(name = "brand_thumbnail", nullable = false)
	private String thumbnail;
	
	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Product> products;
	
	@ManyToOne
	@JoinColumn(name = "status_id")
	@JsonIgnore
	private Status status;
	
}
