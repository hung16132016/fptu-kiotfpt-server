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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;

	@Column(name = "order_time_init", nullable = false)
	private Date timeInit;

	@Column(name = "order_time_complete")
	private Date timeComplete;

	@Column(name = "order_desc", nullable = false)
	private String desc;

	@Column(name = "order_total", nullable = false)
	private float total;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "section_id", referencedColumnName = "section_id")
	private Section section;

	@ManyToOne()
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@ManyToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
}
