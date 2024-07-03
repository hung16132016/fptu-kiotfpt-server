package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.NotifyService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/notify")
public class NotifyController {

	@Autowired
	private NotifyService service;

	@PreAuthorize("hasAnyAuthority('user', 'shop')")
	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllNotifyByAccountID() {
		return service.getNotifyByAccountID();
	}

	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseObject> deleteNotifyByID(@PathVariable int id) {
		return service.deleteNotifyById(id);
	}
}
