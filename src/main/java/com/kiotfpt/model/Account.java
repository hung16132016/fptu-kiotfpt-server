package com.kiotfpt.model;

import java.util.Collection;
import java.util.List;

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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_account")
public class Account implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(role.getValue()));
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
