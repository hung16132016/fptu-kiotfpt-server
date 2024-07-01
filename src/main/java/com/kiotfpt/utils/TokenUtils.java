package com.kiotfpt.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;

@Service
public class TokenUtils {

	public TokenUtils() {
	}

	public static boolean checkMatch(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account currentUser = (Account) authentication.getPrincipal();
		String currentUserRole = currentUser.getRole().getValue();
		return role.equalsIgnoreCase(currentUserRole);
	}

}
