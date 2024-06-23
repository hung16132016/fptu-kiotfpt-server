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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiotfpt.request.CommentRequest;
import com.kiotfpt.response.ProductMiniResponse;
import com.kiotfpt.response.ProfileMiniResponse;

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

	@Transient
	private ProfileMiniResponse profile;

	@Transient
	private ProductMiniResponse productMiniResponse;

	public Comment(CommentRequest request, Account acc, Product product) {
		super();
		this.content = request.getContent();
		this.rate = request.getRate();
		this.date = new Date();
		this.account = acc;
		this.product = product;
	}

	public Comment(Comment comment, AccountProfile profile) {
		super();
		this.id = comment.getId();
		this.content = comment.getContent();
		this.rate = comment.getRate();
		this.date = comment.getDate();
		this.profile = new ProfileMiniResponse(profile);
	}

	public Comment(Comment comment) {
		super();
		this.content = comment.getContent();
		this.rate = comment.getRate();
		this.date = comment.getDate();
		this.account = comment.getAccount();
		this.productMiniResponse = new ProductMiniResponse(comment.getProduct());
	}

}
