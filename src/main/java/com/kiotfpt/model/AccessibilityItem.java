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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kiotfpt_accessibility_item")
public class AccessibilityItem {

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "item_quantity")
	private int quantity;

	@Column(name = "item_total")
	private double total;

	@Column(name = "item_note")
	private String note;

	@ManyToOne()
	@JoinColumn(name = "section_id")
	@JsonIgnore
	private Section section;

	@ManyToOne()
	@JoinColumn(name = "variant_id")
	private Variant variant;

	@ManyToOne()
	@JoinColumn(name = "status_id")
	private Status status;

}
