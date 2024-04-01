package com.kiotfpt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kiotfpt_address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int address_id;

	@Column(name = "address_value", nullable = false)
	private String address_value;

	@ManyToOne()
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne()
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	public Address() {
		super();
	}

	public Address(String address_value, District district, Province province) {
		super();
		this.address_value = address_value;
		this.district = district;
		this.province = province;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String getAddress_value() {
		return address_value;
	}

	public void setAddress_value(String address_value) {
		this.address_value = address_value;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}
}
