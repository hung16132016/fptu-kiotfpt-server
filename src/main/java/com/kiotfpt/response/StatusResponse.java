package com.kiotfpt.response;

public class StatusResponse {

	private int status_id;

	private String value;

	public StatusResponse() {
		super();
	}

	public StatusResponse(int status_id, String value) {
		super();
		this.status_id = status_id;
		this.value = value;
	}

	public int getStatus_id() {
		return status_id;
	}

	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
