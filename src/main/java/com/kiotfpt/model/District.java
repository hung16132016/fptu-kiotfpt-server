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

@Entity
@Table(name = "kiotfpt_district")
public class District {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int district_id;

	@Column(name = "district_value", nullable = false)
	private String district_value;
	
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Address> addresses;

	public District() {
		super();
	}

	public District(String district_value) {
		super();
		this.district_value = district_value;
	}

	public int getDistrict_id() {
		return district_id;
	}

	public String getDistrict_value() {
		return district_value;
	}

	public void setDistrict_value(String district_value) {
		this.district_value = district_value;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}

}
