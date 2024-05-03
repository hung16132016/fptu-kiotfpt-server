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
@Table(name = "kiotfpt_province")
public class Province {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int province_id;
	
	@Column(name = "province_value", nullable = false)
	private String province_value;
	
	@OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Address> addresses;
	
	public Province() {
		super();
	}

	public Province(int province_id, String province_value) {
		super();
		this.province_id = province_id;
		this.province_value = province_value;
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public String getProvince_value() {
		return province_value;
	}

	public void setProvince_value(String province_value) {
		this.province_value = province_value;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}

}
