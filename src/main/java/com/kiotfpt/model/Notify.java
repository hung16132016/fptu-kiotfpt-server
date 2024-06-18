package com.kiotfpt.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@Column(name = "notify_time", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime time;

	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	@JsonIgnore
	private Account account;

	public Notify(Order order, Account account, String string) {
		super();
		if (string == "pending") {
			this.title = "Order pending";
			this.description = "Your order with id " + order.getId() + " is processing, please wait.";
		} else if (string == "completed") {
			this.title = "Order completed";
			this.description = "Your order with id " + order.getId() + " has been delivered.";
		} else if (string == "account welcome") {
			this.title = "Welcome to KiotFPT";
			this.description = "Add to cart the products you love, and checkout your first order.";
		} else if (string == "seller welcome") {
			this.title = "Welcome to KiotFPT";
			this.description = "Welcome to KiotFPT, post your products, we will create orders together.";
		} else {

			this.title = "Order " + string;
			this.description = "Your order with id " + order.getId() + " has been " + string + ".";
		}
		this.account = account;
		this.time = LocalDateTime.now();
	}

}
