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
import com.kiotfpt.model.Voucher;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.VoucherRepository;
import com.kiotfpt.response.VoucherResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class VoucherService {

	@Autowired
	private VoucherRepository repository;

	@Autowired
	private ShopRepository shopRepository;

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
}
