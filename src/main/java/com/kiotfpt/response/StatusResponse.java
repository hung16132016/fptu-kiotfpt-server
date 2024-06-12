package com.kiotfpt.response;

import com.kiotfpt.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {

	private int id;
	private String value;

	public StatusResponse(Status status) {
		super();
		this.id = status.getId();
		this.value = status.getValue();
	}

}
