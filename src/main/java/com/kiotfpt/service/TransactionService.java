package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Transaction;
import com.kiotfpt.repository.TransactionRepository;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.TokenUtils;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repository;

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getTransactionByAccountID() {

		List<Transaction> transactions = repository.findAllByAccount(tokenUtils.getAccount());
		if (!transactions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Transactions found", transactions));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], "Transactions do not exist", new int[0]));

	}
}
