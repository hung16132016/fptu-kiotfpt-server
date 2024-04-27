package com.kiotfpt.request;

public class AccountRequest {
	private String username;
	private String password;
	private int role_id;
	private int status_id;
	public AccountRequest() {
		super();
	}
	public AccountRequest(String username, String password, int role_id, int status_id) {
		super();
		this.username = username;
		this.password = password;
		this.role_id = role_id;
		this.status_id = status_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}
	
	
}
