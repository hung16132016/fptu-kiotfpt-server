package com.kiotfpt.controller;

import java.util.Map;

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
	
	@PostMapping("/sendmail")
	public ResponseEntity<ResponseObject> sendmail(@RequestBody Map<String,String>  map) throws AddressException, MessagingException {
		return service.sendmail(map.get("mail"));
	}

//	// check username
//	@PostMapping("/check-username")
//	public ResponseEntity<ResponseObject> checkus(@RequestBody Account account)
//			throws AddressException, MessagingException {
//		return service.checkus(account);
//	}
//
//	@PostMapping("/check-otp-forgot")
//	public ResponseEntity<ResponseObject> checkOtpForgot(@RequestBody Map<String, String> obj) {
//		return service.checkOtpForgot(obj);
//	}
//
//	@PutMapping("/reset-password")
//	public ResponseEntity<ResponseObject> resetPassword(@RequestBody Map<String, String> obj) {
//		return service.resetPassword(obj);
//	}
//
//	@GetMapping("/confirm-sign-up/{id}")
//	public String confirmSignup(@PathVariable int id) {
//		return service.confirmSignup(id);
//	}
//
//	@GetMapping("/sign-out")
//	public ResponseEntity<ResponseObject> signOut(HttpServletRequest httpRequest) {
//		return service.signOut(httpRequest);
//	}
}

