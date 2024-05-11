package com.kiotfpt.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_account_profile")
public class AccountProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_profile_id")
	private int id;

	@Column(name = "account_profile_name")
	private String name;

	@Column(name = "account_profile_thumbnail")
	private String thumbnail;

	@Column(name = "account_profile_phone")
	private String phone;
 
	@Column(name = "account_profile_email")
	private String email;

	@Column(name = "account_profile_birthday")
	private Date birthday;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnore
	private Account account;
	
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Address> addresses;
}
