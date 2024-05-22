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
			List<VoucherResponse> productResponses = vouchers.stream().map(voucher -> new VoucherResponse())
					.collect(Collectors.toList());

			if (!productResponses.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], "Transactions found", productResponses));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Transactions do not exist", new int[0]));
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
        // Fetch the active Status
        Optional<Status> status = statusRepository.findByValue("active");

        // Create a new Voucher entity
        Voucher voucher = new Voucher();
        voucher.setValue(request.getValue());
        voucher.setStatus(status.get());

        // Save the Voucher entity
        Voucher savedVoucher = repository.save(voucher);

        // Return a successful response
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
                HttpStatus.OK.toString().split(" ")[0], "Voucher created successfully", new VoucherResponse(savedVoucher)));
    }
    
    public ResponseEntity<ResponseObject> updateVoucher(int id, VoucherRequest request) {
        // Check if the voucher exists
        Optional<Voucher> optionalVoucher = repository.findById(id);
        if (!optionalVoucher.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "Voucher with id: " + id + " not found", null));
        }

        Voucher voucher = optionalVoucher.get();

        // Update the Voucher entity
        voucher.setValue(request.getValue());

        // Save the updated Voucher entity
        Voucher updatedVoucher = repository.save(voucher);

        // Return a successful response
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
                HttpStatus.OK.toString().split(" ")[0], "Voucher updated successfully", new VoucherResponse(updatedVoucher)));
    }
}
