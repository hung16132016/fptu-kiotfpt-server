package com.kiotfpt.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kiotfpt_transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transaction_id;
	
	@Column(name = "transaction_time_init", nullable = false)
	private Date transaction_time_init;
	
	@Column(name = "transaction_time_complete")
	private Date transaction_time_complete;
	
	@Column(name = "transaction_desc", nullable = false)
	private String transaction_desc;
	
	@Column(name = "transaction_total", nullable = false)
	private float transaction_total;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "section_id", referencedColumnName = "section_id")
	private Section section;
	
	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;
	
	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	public Transaction() {
		super();
	}

	public Transaction(Date transaction_time_init, Date transaction_time_complete,
			String transaction_desc, float transaction_total, Section section, Shop shop, Account account) {
		super();
		this.transaction_time_init = transaction_time_init;
		this.transaction_time_complete = transaction_time_complete;
		this.transaction_desc = transaction_desc;
		this.transaction_total = transaction_total;
		this.section = section;
		this.shop = shop;
		this.account = account;
	}

	public int getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Date getTransaction_time_init() {
		return transaction_time_init;
	}

	public void setTransaction_time_init(Date transaction_time_init) {
		this.transaction_time_init = transaction_time_init;
	}

	public Date getTransaction_time_complete() {
		return transaction_time_complete;
	}

	public void setTransaction_time_complete(Date transaction_time_complete) {
		this.transaction_time_complete = transaction_time_complete;
	}

	public String getTransaction_desc() {
		return transaction_desc;
	}

	public void setTransaction_desc(String transaction_desc) {
		this.transaction_desc = transaction_desc;
	}

	public float getTransaction_total() {
		return transaction_total;
	}

	public void setTransaction_total(float transaction_total) {
		this.transaction_total = transaction_total;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}
