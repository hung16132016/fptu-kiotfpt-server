package com.kiotfpt.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.AccountRequest;
import com.kiotfpt.request.AccountSignUpRequest;
import com.kiotfpt.service.AuthService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/auth")
public class AuthController {
	@Autowired
	private AuthService service;

	//fix
	@PostMapping("/sign-in")
	public ResponseEntity<ResponseObject> signIn(@RequestBody AccountRequest request) {
		return service.signIn(request.getUsername(), request.getPassword());
	}

	//fix
	@PostMapping("/sign-up")
	public ResponseEntity<ResponseObject> signUp(@RequestBody AccountSignUpRequest request) throws AddressException, MessagingException{
		return service.signUp(request);
	}
	
	@GetMapping("/confirm-sign-up/{username}")
	public ResponseEntity<ResponseObject> activeAccount(@PathVariable String username) throws AddressException, MessagingException{
		return service.activeAccount(username);
	}
	
	@GetMapping("/forgot-password/{username}")
	public ResponseEntity<ResponseObject> forgotPassword(@PathVariable String username) throws AddressException, MessagingException{
		return service.forgotPassword(username);
	}
	
	@GetMapping("/sendmail")
	public ResponseEntity<ResponseObject> sendmail() throws AddressException, MessagingException, JsonProcessingException {
		return service.sendmail();
	}
	
}

