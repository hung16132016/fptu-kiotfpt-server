package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Transaction;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.TransactionRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class TransactionService {

	@Autowired
	private  TransactionRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getTransactionByAccountID(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Transaction> transactions = repository.findAllByAccount(acc.get());
			if (!transactions.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Transactions found", transactions));
			} 
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Transactions do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						responseMessage.get("accountNotFound"), ""));
	}
}
