package com.kiotfpt.model;

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
@Table(name = "kiotfpt_notify")
public class Notify {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notify_id")
	private int id;

	@Column(name = "notify_title", nullable = false)
	private String title;

	@Column(name = "notify_description", nullable = false)
	private String description;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	public Notify(Order order, Account account, String string) {
		super();
		if (string == "pending") {
			this.title = "Order pending";
			this.description = "Your order with id " + order.getId() + " is processing, please wait.";
		} else if (string == "completed") {
			this.title = "Order completed";
			this.description = "Your order with id " + order.getId() + " has been delivered.";
		} else {

			this.title = "Order " + string;
			this.description = "Your order with id " + order.getId() + " has been " + string + ".";
		}
		this.account = account;
	}

}
