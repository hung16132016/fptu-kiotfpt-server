package com.kiotfpt.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kiotfpt.model.ResponseObject;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseObjectHelper {

	public static ResponseEntity<ResponseObject> createFalseResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(new ResponseObject(false, String.valueOf(status.value()), message, null));
	}
	
	public static ResponseEntity<ResponseObject> createTrueResponse(HttpStatus status, String message, Object object) {
		return ResponseEntity.status(status)
				.body(new ResponseObject(true, String.valueOf(status.value()), message, object));
	}
}
