package com.kiotfpt.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

	private int accountId;
	private int voucherId;
	private List<SectionRequest> sections;

}
