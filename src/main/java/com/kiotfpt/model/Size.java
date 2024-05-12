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
@Table(name = "kiotfpt_size")
public class Size {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "size_id", nullable = false)
	private int id;

	@Column(name = "size_value", nullable = false)
	private String value;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Variant> repo;
}
