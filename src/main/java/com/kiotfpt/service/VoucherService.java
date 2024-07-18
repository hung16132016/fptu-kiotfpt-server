package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Voucher;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.VoucherRepository;
import com.kiotfpt.request.VoucherRequest;
import com.kiotfpt.response.VoucherResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class VoucherService {

	@Autowired
	private VoucherRepository repository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private StatusRepository statusRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getVoucherByShopID(int id) {
		Optional<Shop> shop = shopRepository.findById(id);
		if (!shop.isEmpty()) {
			List<Voucher> vouchers = repository.findAllByShop(shop.get());
			List<VoucherResponse> productResponses = vouchers.stream().map(voucher -> new VoucherResponse(voucher))
					.collect(Collectors.toList());

			if (!productResponses.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], "Vouchers found", productResponses));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Vouchers do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop not found", ""));
	}

	public ResponseEntity<ResponseObject> getVoucherById(int id) {
		return repository.findById(id)
				.map(voucher -> ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Voucher found",
								new VoucherResponse(voucher))))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Voucher with id: " + id + " not found", null)));
	}

	public ResponseEntity<ResponseObject> deleteVoucherById(int id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Voucher with id: " + id + " not found", null));
		}

		repository.deleteById(id);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Delete voucher successful", null));
	}

	public ResponseEntity<ResponseObject> createVoucher(VoucherRequest request) {
		int value = request.getValue();
		if (value <= 0 || value > 100) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Voucher value must be between 1 and 100", null));
		}

		// Fetch the active Status
		Optional<Status> status = statusRepository.findByValue("active");
		if (status.isEmpty()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"Active status not found in database", null));
		}

		// Fetch the shop by ID
		Optional<Shop> shop = shopRepository.findById(request.getShop_id());
		if (shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"Shop with ID: " + request.getShop_id() + " not found", null));
		}

		// Create a new Voucher entity
		Voucher voucher = new Voucher();
		voucher.setValue(request.getValue());
		voucher.setStatus(status.get());
		voucher.setShop(shop.get());

		// Save the Voucher entity
		Voucher savedVoucher = repository.save(voucher);

		// Return a successful response
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Voucher created successfully",
						new VoucherResponse(savedVoucher)));
	}

	public ResponseEntity<ResponseObject> updateVoucher(int id, VoucherRequest request) {
		int value = request.getValue();
		
		// Check if the voucher exists
		Optional<Voucher> optionalVoucher = repository.findById(id);
		if (!optionalVoucher.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Voucher with id: " + id + " not found", null));
		}
		
		if (value <= 0 || value > 100) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Voucher value must be between 1 and 100", null));
		}

		Voucher voucher = optionalVoucher.get();

		// Update the Voucher entity
		voucher.setValue(request.getValue());

		// Save the updated Voucher entity
		Voucher updatedVoucher = repository.save(voucher);

		// Return a successful response
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Voucher updated successfully",
						new VoucherResponse(updatedVoucher)));
	}
	
	public ResponseEntity<ResponseObject> deactivateVoucher(int id) {

		Voucher voucher = repository.findById(id).orElseThrow(() -> new RuntimeException("Voucher not found"));
		Status inactiveStatus = statusRepository.findByValue("inactive")
				.orElseThrow(() -> new RuntimeException("Inactive status not found"));
		voucher.setStatus(inactiveStatus);
		repository.save(voucher);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Voucher deleted successfully", null));
	}

	public ResponseEntity<ResponseObject> activateVoucher(int id) {
		Optional<Voucher> optionalVoucher = repository.findByIdAndStatusValue(id, "inactive");

		if (optionalVoucher.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Voucher not found or already active", null));
		}

		Voucher voucher = optionalVoucher.get();
		Optional<Status> activeStatus = statusRepository.findByValue("active");

		if (activeStatus.isEmpty()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(false,
					HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0], "Active status not found", null));
		}

		voucher.setStatus(activeStatus.get());
		repository.save(voucher);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Voucher activated successfully", null));
	}
}
