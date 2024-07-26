package com.kiotfpt.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.Blacklist;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.BlacklistRepository;
import com.kiotfpt.request.AccountRequest;
import com.kiotfpt.request.AccountSignUpRequest;
import com.kiotfpt.service.AuthService;
import com.kiotfpt.service.JwtService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/auth")
public class AuthController {
	@Autowired
	private AuthService service;

	@Autowired
	private BlacklistRepository blacklistRepository;

	@Autowired
	private JwtService jwtService;

	// fix
	@PostMapping("/sign-in")
	public ResponseEntity<ResponseObject> signIn(@RequestBody AccountRequest request) {
		return service.signIn(request.getUsername(), request.getPassword());
	}

	// fix
	@PostMapping("/sign-up")
	public ResponseEntity<ResponseObject> signUp(@RequestBody AccountSignUpRequest request)
			throws AddressException, MessagingException {
		return service.signUp(request);
	}

	@GetMapping("/confirm-sign-up/{username}")
	public ResponseEntity<ResponseObject> activeAccount(@PathVariable String username)
			throws AddressException, MessagingException {
		return service.activeAccount(username);
	}

	@GetMapping("/forgot-password/{username}")
	public ResponseEntity<ResponseObject> forgotPassword(@PathVariable String username)
			throws AddressException, MessagingException {
		return service.forgotPassword(username);
	}

	@GetMapping("/sendmail")
	public ResponseEntity<ResponseObject> sendmail()
			throws AddressException, MessagingException, JsonProcessingException {
		return service.sendmail();
	}

	@PostMapping("/sign-out")
	public ResponseEntity<ResponseObject> logout() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Account currentUser = (Account) authentication.getPrincipal();
			String currentToken = jwtService.getCurrentToken();

			if (currentToken == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "No token found", null));
			}

			Blacklist blacklist = new Blacklist();
			blacklist.setToken(currentToken);
			blacklist.setAccountId(currentUser.getId());
			blacklistRepository.save(blacklist);

			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Logged out successfully", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"An error occurred while logging out", null));
		}
	}

	@GetMapping("/confirm-forgot-password")
	public ResponseEntity<ResponseObject> confirmChangePassword(@RequestParam String newPassword,
			@RequestParam String username) {
		return service.confirmChangePassword(newPassword, username);
	}
}
