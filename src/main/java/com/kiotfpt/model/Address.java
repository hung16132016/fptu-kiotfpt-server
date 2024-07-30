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
@Table(name = "kiotfpt_address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private int id;

	@Column(name = "address_value", nullable = false)
	private String value;
	
	@Column(name = "address_default", nullable = false)
	private boolean isdefault;

	@ManyToOne()
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne()
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;
	
	@ManyToOne()
	@JoinColumn(name = "account_profile_id", nullable = false)
	private AccountProfile profile;
	
	@OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Order> orders;

	public Address(String value, boolean isdefault, District district, Province province, AccountProfile profile,
			Collection<Order> orders) {
		super();
		this.value = value;
		this.isdefault = isdefault;
		this.district = district;
		this.province = province;
		this.profile = profile;
		this.orders = orders;
	}
}
