package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Address;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AddressRepository;
import com.kiotfpt.response.AddressResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	@Autowired
	private AccountProfileRepository accountprofileRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAddressByAccountID(int account_id) {
		Optional<AccountProfile> acc = accountprofileRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Address> addresses = repository.findAllByProfile(acc.get());
			if (!addresses.isEmpty()) {

				List<AddressResponse> list = new ArrayList<AddressResponse>();
				for (Address address : addresses) {
					list.add(new AddressResponse(address));
				}
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Addresses found", list));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Addresses do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), ""));
	}
}
