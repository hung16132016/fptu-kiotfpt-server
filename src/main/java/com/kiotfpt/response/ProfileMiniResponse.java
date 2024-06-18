package com.kiotfpt.response;

import com.kiotfpt.model.AccountProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMiniResponse {

	private String name;
	private String phone;
	private String avatar;
	private String email;
	
	public ProfileMiniResponse(AccountProfile profile) {
		super();
		this.name = profile.getName();
		this.phone = profile.getPhone();
		this.avatar = profile.getThumbnail();
		this.email = profile.getEmail();
	}

}
