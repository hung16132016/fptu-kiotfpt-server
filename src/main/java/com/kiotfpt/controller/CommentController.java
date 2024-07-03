package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.CommentRequest;
import com.kiotfpt.service.CommentService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/comment")
public class CommentController {

	@Autowired
	private CommentService service;

	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/create")
	public ResponseEntity<ResponseObject> getVoucherByShopID(@RequestBody CommentRequest request) {
		return service.createComment(request);
	}

	@GetMapping("/get-all")
	public ResponseEntity<ResponseObject> getCommentsByAccountId(@RequestParam(name = "accountID") int accountID) {
		return service.getAllCommentsByAccountId(accountID);
	}
}
