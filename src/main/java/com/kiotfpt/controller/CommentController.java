package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@PostMapping("/create")
	public ResponseEntity<ResponseObject> getVoucherByShopID(@RequestBody CommentRequest request) {
		return service.createComment(request);
	}
}
