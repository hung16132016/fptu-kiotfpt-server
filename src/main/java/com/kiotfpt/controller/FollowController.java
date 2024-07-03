package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.service.FollowService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping("/v1/follow")
public class FollowController {

	@Autowired
	private FollowService service;

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("")
	public ResponseEntity<ResponseObject> followShop(@RequestParam(name = "shopId", required = true) int shopId,
			@RequestParam(name = "status", required = true) String status) {
		return service.followShop(shopId, status);
	}

	@GetMapping("/check")
	public ResponseEntity<ResponseObject> checkFollowShop(@RequestParam(name = "shopId", required = true) int shopId,
			@RequestParam(name = "accountId", required = true) int accountId) {
		return service.checkfollowShop(shopId, accountId);
	}
}
