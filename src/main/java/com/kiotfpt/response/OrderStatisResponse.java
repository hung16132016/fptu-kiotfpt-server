package com.kiotfpt.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisResponse {
	private int id;
	private float total;
	private Date timeComplete;
}