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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kiotfpt.response.TransactionDesc;

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

	@Column(name = "transaction_desc", nullable = false, length = 10000)
	private String desc;

	@Column(name = "transaction_total", nullable = false)
	private float total;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	public Transaction(Order order) throws JsonProcessingException {
		super();
		this.timeInit = order.getTimeInit();
		this.timeComplete = order.getTimeComplete();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.desc = objectMapper.writeValueAsString(new TransactionDesc(order));
		this.total = order.getTotal();
		this.shop = order.getShop();
		this.account = order.getAccount();
	}

}
