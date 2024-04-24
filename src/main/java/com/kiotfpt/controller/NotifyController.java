package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.NotifyService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/v1/notify")
public class NotifyController {

	@Autowired
	private NotifyService service;

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getAllNotifyByAccountID(@RequestParam(name = "accountID") int id) {
		return service.getNotifyByAccountID(id);
	}
}
