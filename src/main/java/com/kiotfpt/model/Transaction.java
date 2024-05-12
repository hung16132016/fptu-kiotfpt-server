package com.kiotfpt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private int id;

	@Column(name = "transaction_time_init", nullable = false)
	private Date timeInit;

	@Column(name = "transaction_time_complete")
	private Date timeComplete;

	@Column(name = "transaction_desc", nullable = false)
	private String desc;

	@Column(name = "transaction_total", nullable = false)
	private float total;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
}
