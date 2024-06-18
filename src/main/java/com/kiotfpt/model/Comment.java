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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiotfpt.request.CommentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id", nullable = false)
	private int id;

	@Column(name = "comment_content", nullable = false)
	private String content;

	@Column(name = "comment_rate", nullable = false)
	private float rate;

	@Column(name = "comment_date", nullable = false)
	private Date date;

	@ManyToOne()
	@JsonIgnore
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "product_id")
	private Product product;

	public Comment(CommentRequest request, Account acc, Product product) {
		super();
		this.content = request.getContent();
		this.rate = request.getRate();
		this.date = new Date();
		this.account = acc;
		this.product = product;
	}

}
