package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Notify;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.NotifyRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class NotifyService {

	@Autowired
	private  NotifyRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getNotifyByAccountID(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Notify> notifies = repository.findAllByAccount(acc.get());
			if (!notifies.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Notifies found", notifies));
			} 
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Notifies do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						responseMessage.get("accountNotFound"), ""));
	}
}
