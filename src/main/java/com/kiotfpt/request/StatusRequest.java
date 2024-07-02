package com.kiotfpt.request;

import com.kiotfpt.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {

	private String value;

	public StatusRequest(Status status) {
		super();
		this.value = status.getValue();
	}

}
