package com.kiotfpt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private int id;

	@Column(name = "address_value", nullable = false)
	private String value;

	@ManyToOne()
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne()
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;
	
	@ManyToOne()
	@JoinColumn(name = "account_profile_id", nullable = false)
	private AccountProfile profile;
}
