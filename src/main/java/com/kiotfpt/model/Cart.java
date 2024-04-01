package com.kiotfpt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kiotfpt_cart")
public class Cart {

    @EmbeddedId
    private CartId cartId;

	@ManyToOne
	@MapsId("account_id")
	@JsonIgnore
	private Account account;

	@ManyToOne
	@MapsId("section_id")
	@JsonIgnore
	private Section section;

}

@Embeddable
class CartId implements Serializable {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "section_id")
    private Long sectionId;

	public CartId() {
		super();
	}

	public CartId(Long accountId, Long sectionId) {
		super();
		this.accountId = accountId;
		this.sectionId = sectionId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

}
