package com.kiotfpt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Size;
import com.kiotfpt.repository.SizeRepository;

@Service
public class SizeService {

	@Autowired
	private SizeRepository repository;

	public ResponseEntity<ResponseObject> getAllSize() {
		List<Size> sizes = repository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Colors fetched successfully", sizes));
	}
}
