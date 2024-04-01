package com.kiotfpt.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_account_profile")
public class AccountProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int account_profile_id;

	@Column(name = "account_profile_name", nullable = false)
	private String account_profile_name;

	@Column(name = "account_profile_thumbnail", nullable = false)
	private String account_profile_thumbnail;

	@Column(name = "account_profile_phone", nullable = false)
	private String account_profile_phone;

	@Column(name = "account_profile_email", nullable = false)
	private String account_profile_email;

	@Column(name = "account_profile_birthday", nullable = false)
	private Date account_profile_birthday;

	@Column(name = "account_profile_address", nullable = false)
	private String account_profile_address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnore
	private Account account;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address address;

	public AccountProfile() {
		super();
	}

	public AccountProfile(String account_profile_thumbnail, String account_profile_name, Date account_profile_birthday,
			String account_profile_phone, String account_profile_email, String account_profile_address) {
		super();
		this.account_profile_thumbnail = account_profile_thumbnail;
		this.account_profile_name = account_profile_name;
		this.account_profile_birthday = account_profile_birthday;
		this.account_profile_phone = account_profile_phone;
		this.account_profile_email = account_profile_email;
		this.account_profile_address = account_profile_address;
	}

	public int getAccount_profile_id() {
		return account_profile_id;
	}

	public void setAccount_profile_id(int account_profile_id) {
		this.account_profile_id = account_profile_id;
	}

	public String getAccount_profile_thumbnail() {
		return account_profile_thumbnail;
	}

	public void setAccount_profile_thumbnail(String account_profile_thumbnail) {
		this.account_profile_thumbnail = account_profile_thumbnail;
	}

	public String getAccount_profile_name() {
		return account_profile_name;
	}

	public void setAccount_profile_name(String account_profile_name) {
		this.account_profile_name = account_profile_name;
	}

	public Date getAccount_profile_birthday() {
		return account_profile_birthday;
	}

	public void setAccount_profile_birthday(Date account_profile_birthday) {
		this.account_profile_birthday = account_profile_birthday;
	}

	public String getAccount_profile_phone() {
		return account_profile_phone;
	}

	public void setAccount_profile_phone(String account_profile_phone) {
		this.account_profile_phone = account_profile_phone;
	}

	public String getAccount_profile_email() {
		return account_profile_email;
	}

	public void setAccount_profile_email(String account_profile_email) {
		this.account_profile_email = account_profile_email;
	}

	public String getAccount_profile_address() {
		return account_profile_address;
	}

	public void setAccount_profile_address(String account_profile_address) {
		this.account_profile_address = account_profile_address;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
