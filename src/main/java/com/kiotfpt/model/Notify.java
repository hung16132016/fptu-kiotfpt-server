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
@Table(name = "kiotfpt_notify")
public class Notify {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int notify_id;
	
	@Column(name = "notify_title", nullable = false)
	private String notify_title;

	@Column(name = "notify_description", nullable = false)
	private String notify_description;
	
	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	public Notify() {
		
	};

	public Notify(int notify_id, String notify_title, String notify_description, Account account) {
		super();
		this.notify_id = notify_id;
		this.notify_title = notify_title;
		this.notify_description = notify_description;
		this.account = account;
	}

	public int getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(int notify_id) {
		this.notify_id = notify_id;
	}

	public String getNotify_title() {
		return notify_title;
	}

	public void setNotify_title(String notify_title) {
		this.notify_title = notify_title;
	}

	public String getNotify_description() {
		return notify_description;
	}

	public void setNotify_description(String notify_description) {
		this.notify_description = notify_description;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	
}
