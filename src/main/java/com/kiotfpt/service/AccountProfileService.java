package com.kiotfpt.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class AccountProfileService {

	@Autowired
	private AccountProfileRepository repository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();
	
	public ResponseEntity<ResponseObject> getProfileByAccountID(int id) {
		Optional<Account> account = accountRepository.findById(id);		
		if (account.isPresent()) {
			if (account.get().getStatus().getValue().equals("inactive")) 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("accountNotActivate"), new int[0]));
			
			Optional<AccountProfile> profile = repository.findByAccount(account.get());
			if (profile.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("profileFound"), profile.get()));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("profileNotFound"), ""));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
			HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("profileNotFound"), ""));
	}
	
	
}
