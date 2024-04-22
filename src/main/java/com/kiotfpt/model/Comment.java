package com.kiotfpt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int comment_id;
	
	@Column(name = "comment_content", nullable = false)
	private String comment_content;
	
	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public Comment() {
		super();
	}

	public Comment(int comment_id, String comment_content) {
		super();
		this.comment_id = comment_id;
		this.comment_content = comment_content;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
