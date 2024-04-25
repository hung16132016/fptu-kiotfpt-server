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
import com.kiotfpt.response.DistrictResponse;
import com.kiotfpt.response.ProvinceResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class AddressService {

	@Autowired
	private  AddressRepository repository;

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

					DistrictResponse district = new DistrictResponse();
					ProvinceResponse province = new ProvinceResponse();
					AddressResponse response = new AddressResponse();
					
					district.setDistrict_id(address.getDistrict().getDistrict_id());
					district.setDistrict_value(address.getDistrict().getDistrict_value());
					
					province.setProvince_id(address.getProvince().getProvince_id());
					province.setProvince_value(address.getProvince().getProvince_value());
					
					response.setAddress_id(address.getAddress_id());
					response.setAddress_value(address.getAddress_value());
					response.setDistrict(district);
					response.setProvince(province);
					
					list.add(response);
				}
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Addresses found", list));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Addresses do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						responseMessage.get("accountNotFound"), ""));
	}
}
