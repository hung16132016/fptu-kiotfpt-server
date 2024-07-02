package com.kiotfpt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.repository.BlacklistRepository;
import com.kiotfpt.service.JwtService;

@Service
public class TokenUtils {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private BlacklistRepository blacklistRepository;

	public boolean checkMatch(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account currentUser = (Account) authentication.getPrincipal();
		String currentToken = jwtService.getCurrentToken();

		if (currentToken == null) {
			return false; // No token found
		}

		// Check if current token is in blacklist
		boolean tokenInBlacklist = blacklistRepository.existsByTokenAndAccountId(currentToken, currentUser.getId());
		if (tokenInBlacklist) {
			return false; // Token is blacklisted, deny access
		}

		// Proceed with role checking
		String currentUserRole = currentUser.getRole().getValue();
		return role.equalsIgnoreCase(currentUserRole);
	}

	public Account getAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (Account) authentication.getPrincipal();
	}
}
