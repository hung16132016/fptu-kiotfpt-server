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
import com.kiotfpt.model.Province;
import com.kiotfpt.model.District;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AddressRepository;
import com.kiotfpt.repository.DistrictRepository;
import com.kiotfpt.repository.ProvinceRepository;
import com.kiotfpt.request.AddressRequest;
import com.kiotfpt.response.AddressResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	@Autowired
	private AccountProfileRepository accountprofileRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private DistrictRepository districtRepository;

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
	
	public ResponseEntity<ResponseObject> getAddressByID(int address_id) {
		Optional<Address> address = repository.findById(address_id);
		return !address.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", address))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	
	public ResponseEntity<ResponseObject> createAddress(AddressRequest request) {
		Optional<AccountProfile> profile = accountprofileRepository.findById(request.getAccount_profile_id());
		if (profile.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Profile is not exist", new int[0]));
		
		Optional<Province> province = provinceRepository.findById(request.getProvince_id());
		if (province.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Province is not exist", new int[0]));
		
		Optional<District> district = districtRepository.findById(request.getDistrict_id());
		if (district.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "District is not exist", new int[0]));
		
		if (request.getAddress_value().length() == 0 )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input cannot be empty!", null));
		if (request.getAddress_value().length() == 255) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input is too long!", null));
		
		Address newAddress = new Address();
		newAddress.setDistrict(district.get());
		newAddress.setProvince(province.get());
		newAddress.setProfile(profile.get());
		newAddress.setValue(request.getAddress_value());
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
				"New address is added successfully", repository.save(newAddress)));
	}
	
	public ResponseEntity<ResponseObject> updateAddress(AddressRequest request) {
		Optional<Address> address = repository.findById(request.getAddress_id());
		if (address.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Address is not exist", new int[0]));
		
		Optional<Province> province = provinceRepository.findById(request.getProvince_id());
		if (province.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Province is not exist", new int[0]));
		
		Optional<District> district = districtRepository.findById(request.getDistrict_id());
		if (district.isEmpty()) 
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "District is not exist", new int[0]));
		
		if (request.getAddress_value().length() == 0 )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input cannot be empty!", null));
		if (request.getAddress_value().length() == 255) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, "400", "Input is too long!", null));
		
		Address updateAddress = address.get();
		updateAddress.setDistrict(district.get());
		updateAddress.setProvince(province.get());
		updateAddress.setValue(request.getAddress_value());
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
				"New address is added successfully", repository.save(updateAddress)));
	}
	
	public ResponseEntity<ResponseObject> deleteAddress(int address_id) {
		Optional<Address> address = repository.findById(address_id);
		if (address.isEmpty()) 
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
		
		repository.deleteById(address_id);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
				"Address is delete successfully", ""));
	}
	
	public ResponseEntity<ResponseObject> getAllProvince() {
		List<Province> provinces = provinceRepository.findAll();
		return !provinces.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", provinces))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}
	
	public ResponseEntity<ResponseObject> getAllDistrictByProvinceID(int province_id) {
		Optional<Province> province = provinceRepository.findById(province_id);
		if(!province.isEmpty()) {
			List<District> districts = districtRepository.findAllByProvince(province.get());
			if (districts.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						"There is no district with this province", districts));
			}
			return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
					"There are districts with this province", districts));
		};
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							"Could not find any province with this ID", province.get()));
		
	}
}
