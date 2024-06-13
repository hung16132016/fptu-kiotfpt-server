package com.kiotfpt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Color;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.ColorRepository;

@Service
public class ColorService {

	@Autowired
	private ColorRepository repository;

	public ResponseEntity<ResponseObject> getAllColors() {
		List<Color> colors = repository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Colors fetched successfully", colors));
	}
}
