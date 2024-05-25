package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {

	private int profile_id;
	private String oldPassword;
	private String newPassword;
	private String retypePassword;
}
