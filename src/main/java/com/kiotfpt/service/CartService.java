package com.kiotfpt.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Cart;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.CartRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class CartService {

	@Autowired
	private CartRepository repository;
//	@Autowired
//	private AddressDeliverRepository addRepository;
	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

//	public ResponseEntity<ResponseObject> getCartByAccount(int account_id) {
//			List<Cart> carts = repository.findAllByAccountId(account_id);
//			return !carts.isEmpty()
//					? ResponseEntity.status(HttpStatus.OK)
//							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//									responseMessage.get("cartFound"), carts.get(0)))
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//							HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), ""));
//	}
	
	public ResponseEntity<ResponseObject> getCartByID(int cart_id) {
		Optional<Cart> cart = repository.findById(cart_id);
		return !cart.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								responseMessage.get("cartFound"), cart.get()))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), ""));
}

//	public ResponseEntity<ResponseObject> createCart(Map<String, String> map) {
//		Optional<Account> account = accountRepository.findById(Integer.parseInt(map.get("account_id")));
//		Optional<AddressDeliver> address = addRepository.findById(Integer.parseInt(map.get("address_id")));
//		Cart cart = new Cart();
//		cart.setAddress(address.get());
//		cart.setAccount(account.get());
//		cart.setStatus(map.get("status"));
//		cart.setTotal(Double.parseDouble(map.get("total")));
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.body(new ResponseObject(true, HttpStatus.CREATED.toString().split(" ")[0],
//						responseMessage.get("createCartSuccess"), repository.save(cart)));
//	}
//
//	public ResponseEntity<ResponseObject> updateCart(Cart newCart) {
//		Cart updateCart = repository.findById(newCart.getId()).map(Cart -> {
//			Cart.setStatus(newCart.getStatus());
//			Cart.setTotal(newCart.getTotal());
//			return repository.save(Cart);
//		}).orElseGet(() -> {
//			return null;
//		});
//		if (updateCart == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("CartNotFound"), ""));
//		} else {
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(false,
//					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("updateCartSuccess"), updateCart));
//		}
//	}

}
