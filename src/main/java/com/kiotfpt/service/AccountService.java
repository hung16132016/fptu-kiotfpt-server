package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;

@Service
public class AccountService {
	@Autowired
	private AccountRepository repository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllAccount() {

		List<Account> accounts = repository.findAll();

		return !accounts.isEmpty()
				? ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Data has found successfully", accounts)

				: ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "No accounts");

	}

}
