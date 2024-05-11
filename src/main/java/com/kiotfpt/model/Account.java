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
@Table(name = "kiotfpt_account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "account_username", nullable = false)
	private String username;

	@Column(name = "account_password", nullable = false)
	private String password;

	@ManyToOne()
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Order> orders;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Transaction> transactions;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Notify> notifies;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Comment> comments;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<Follow> follows;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<ProductFavourite> favourite;

}
