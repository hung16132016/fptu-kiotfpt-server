package com.kiotfpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.request.VoucherRequest;
import com.kiotfpt.service.VoucherService;

@CrossOrigin(origins = "http://localhost:8888")
@RestController
@RequestMapping(path = "/v1/voucher")
public class VoucherController {

	@Autowired
	private VoucherService service;

	@GetMapping("/get-by-shop")
	public ResponseEntity<ResponseObject> getVoucherByShopID(@RequestParam(name = "shopID") int id) {
		return service.getVoucherByShopID(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getVoucherById(@PathVariable int id) {
		return service.getVoucherById(id);
	}

	@PreAuthorize("hasAuthority('shop')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseObject> deleteVoucherById(@PathVariable int id) {
		return service.deleteVoucherById(id);
	}

	@PreAuthorize("hasAuthority('shop')")
	@PostMapping("/create")
	public ResponseEntity<ResponseObject> createVoucher(@RequestBody VoucherRequest request) {
		return service.createVoucher(request);
	}

	@PreAuthorize("hasAuthority('shop')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseObject> updateVoucher(@PathVariable int id, @RequestBody VoucherRequest request) {
		return service.updateVoucher(id, request);
	}
}
