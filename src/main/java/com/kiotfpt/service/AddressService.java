package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Address;
import com.kiotfpt.model.District;
import com.kiotfpt.model.Province;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AddressRepository;
import com.kiotfpt.repository.DistrictRepository;
import com.kiotfpt.repository.ProvinceRepository;
import com.kiotfpt.request.AddressRequest;
import com.kiotfpt.response.AddressResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

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

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAddressByAccountID() {
		Optional<AccountProfile> acc = accountprofileRepository.findByAccount(tokenUtils.getAccount());
		if (acc.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					responseMessage.get("accountNotFound"));

		List<Address> addresses = repository.findAllByProfile(acc.get());
		if (!addresses.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Addresses do not exist");

		List<AddressResponse> list = new ArrayList<AddressResponse>();
		for (Address address : addresses) {
			list.add(new AddressResponse(address));
		}
		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Addresses found", list);

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
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Profile is not exist");

		Optional<AccountProfile> profileToken = accountprofileRepository.findByAccount(tokenUtils.getAccount());

		if (profile.get().getId() != profileToken.get().getId())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");

		Optional<Province> province = provinceRepository.findById(request.getProvince_id());
		if (province.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Province is not exist");

		Optional<District> district = districtRepository.findById(request.getDistrict_id());
		if (district.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "District is not exist");

		if (request.getAddress_value().length() == 0)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input cannot be empty!");
		if (request.getAddress_value().length() == 255)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input is too long!");

		Address newAddress = new Address();
		newAddress.setDistrict(district.get());
		newAddress.setProvince(province.get());
		newAddress.setProfile(profile.get());
		newAddress.setValue(request.getAddress_value());
		if (request.isDefault()) {
			Collection<Address> collection = profile.get().getAddresses();
			List<Address> listAllAddress = new ArrayList(collection);
			for (Address list : listAllAddress) {
				list.setIsdefault(false);
				repository.save(list);
			}
			newAddress.setIsdefault(true);
		} else {
			newAddress.setIsdefault(false);
		}

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "New address is added successfully",
				repository.save(newAddress));
	}

	public ResponseEntity<ResponseObject> updateAddress(AddressRequest request) {
		Optional<Address> address = repository.findById(request.getAddress_id());
		if (address.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Address is not exist");

		Optional<Province> province = provinceRepository.findById(request.getProvince_id());
		if (province.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Province is not exist");

		Optional<District> district = districtRepository.findById(request.getDistrict_id());
		if (district.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "District is not exist");

		if (request.getAddress_value().length() == 0)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input cannot be empty!");
		if (request.getAddress_value().length() == 255)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input is too long!");

		Address updateAddress = address.get();
		updateAddress.setDistrict(district.get());
		updateAddress.setProvince(province.get());
		updateAddress.setValue(request.getAddress_value());

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Address is updated successfully",
				repository.save(updateAddress));
	}

	public ResponseEntity<ResponseObject> deleteAddress(int address_id) {
		Optional<Address> address = repository.findById(address_id);
		if (address.isEmpty())
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));

		repository.deleteById(address_id);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Address is delete successfully", ""));
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
		if (!province.isEmpty()) {
			List<District> districts = districtRepository.findAllByProvince(province.get());
			if (districts.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"There is no district with this province", districts));
			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "There are districts with this province", districts));
		}
		;
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
						"Could not find any province with this ID", province.get()));

	}

	public ResponseEntity<ResponseObject> setAddressDefaultByID(int address_id) {
		Optional<Address> address = repository.findById(address_id);
		if (address.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "There is no address with this ID", new int[0]));

		Collection<Address> collection = address.get().getProfile().getAddresses();
		List<Address> listAllAddress = new ArrayList(collection);
		for (Address list : listAllAddress) {
			if (list.getId() == address_id) {
				list.setIsdefault(true);
			} else {
				list.setIsdefault(false);
			}
			repository.save(list);
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Data has found successfully", address));

	}
}
