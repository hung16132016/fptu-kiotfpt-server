package com.kiotfpt.request;

public class AccountSignUpRequest {

	private String username;
	private String password;
	private String retypePassword;
	public AccountSignUpRequest() {
		super();
	}
	public AccountSignUpRequest(String username, String password, String retypePassword) {
		super();
		this.username = username;
		this.password = password;
		this.retypePassword = retypePassword;
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
	public String getRetypePassword() {
		return retypePassword;
	}
	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
	
}
