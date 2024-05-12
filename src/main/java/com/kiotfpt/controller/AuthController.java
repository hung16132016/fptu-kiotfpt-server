package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.AccountRequest;
import com.kiotfpt.request.AccountSignUpRequest;
import com.kiotfpt.service.AuthService;

@CrossOrigin(origins = "http://localhost:8080")
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
	public ResponseEntity<ResponseObject> signUp(@RequestBody AccountSignUpRequest request) {
		return service.signUp(request);
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

