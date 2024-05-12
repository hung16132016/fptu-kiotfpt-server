package com.kiotfpt.request;

import com.kiotfpt.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {

	private int id;
	private String value;

	public StatusRequest(Status status) {
		super();
		this.id = status.getId();
		this.value = status.getValue();
	}

}
