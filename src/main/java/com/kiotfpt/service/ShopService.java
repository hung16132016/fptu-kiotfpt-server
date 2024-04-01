package com.kiotfpt.service;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class ShopService {
	@Autowired
	private ShopRepository repository;
//	@Autowired
//	private AccountRepository accountRepository;
	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getShopByID(HttpServletRequest request, int id) {
		Optional<Shop> shop = repository.findById(id);
		if (shop.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shop.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}
//	public ResponseEntity<ResponseObject> getAllShop(HttpServletRequest request) {
//		String token = "";
//		try {
//			token = request.getHeader("Authorization").split(" ")[1];
//		} catch (NullPointerException e) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//		Account acc = accountRepository.findByToken(token);
//		if (acc != null) {
//			if (acc.getRole().getID() == 3) {
//			List<Shop> accounts = repository.findAll();
//			return !accounts.isEmpty()
//					? ResponseEntity.status(HttpStatus.OK)
//							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//									responseMessage.get("shopFound"), accounts))
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//							HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
//			} else {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//						HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//			}
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//	}

//	public ResponseEntity<ResponseObject> createShop(Map<String, String> shop) {
//		Optional<Account> account = accountRepository.findById(Integer.parseInt(shop.get("id")));
//		Shop newshop = new Shop();
//		if (account.isPresent()) {
//			newshop.setAddress(shop.get("address"));
//			newshop.setEmail(shop.get("email"));
//			newshop.setName(shop.get("name"));
//			newshop.setPhone(shop.get("phone"));
//			newshop.setRating(0.0);
//			newshop.setStatus(1);
//			newshop.setLevel(0);
//			newshop.setAccount(account.get());
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//					HttpStatus.OK.toString().split(" ")[0], "Create shop successful", repository.save(newshop)));
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//				new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop has not found", ""));
//	}
//
	public ResponseEntity<ResponseObject> updateShop(HttpServletRequest request, int id, Shop shop) {
		Optional<Shop> foundshop = repository.findById(id);
		if (foundshop.isPresent()) {
			foundshop.get().setAddress(shop.getAddress());
			foundshop.get().setShop_email(shop.getShop_email());
			foundshop.get().setShop_name(shop.getShop_name());
			foundshop.get().setShop_phone(shop.getShop_phone());
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
							responseMessage.get("updateShopSuccess"), repository.save(foundshop.get())));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}
//
//	public ResponseEntity<ResponseObject> getShopByAccountID(HttpServletRequest request, int id) {
//		Optional<Shop> shop = repository.findByAccountID(id);
//		if (shop.isPresent()) {
//			String token = "";
//			try {
//				token = request.getHeader("Authorization").split(" ")[1];
//			} catch (NullPointerException e) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//						HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//			}
//
//			if (token.equals(shop.get().getAccount().getToken())) {
//				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//						HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getShopByIdSuccess"), shop.get()));
//			} else {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//						HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//			}
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("getShopByAccountIdFail"), ""));
//	}
//
//	public ResponseEntity<ResponseObject> getAllShopRevenueByTime(HttpServletRequest request, int month) {
//		Account accToken = null;
//		try {
//			accToken = accountRepository.findByToken(request.getHeader("Authorization").split(" ")[1]);
//		} catch (NullPointerException e) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//
//		if (accToken == null || accToken.getRole().getID() != 3) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//		List<Shop> shops = repository.findAll();
//		double sum = 0;
//		List<Map<String, Object>> shopDataList = new ArrayList<>();
//		for (Shop s : shops) {
//			Map<String, Object> shopData = new HashMap<>();
//			for (Transaction j : s.getTransaction()) {
//				if (j.getTime().getMonth() + 1 == month) {
//					sum = sum + j.getTotal();
//				}
//			}
//			if(sum == 0) {
//				continue;
//			}
//			shopData.put("shop", s);
//			shopData.put("revenue", sum);
//			sum = 0;
//			shopDataList.add(shopData);
//		}
//		if (shopDataList.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
//							responseMessage.get("notFoundDataMonth") + month + "!", ""));
//		}
//
//		if (month < 1 || month > 12) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("inputMonthFail"), ""));
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shopDataList));
//	}
//
//	public ResponseEntity<ResponseObject> getTotalRevenueByTime(HttpServletRequest request, int month) {
//		Account accToken = null;
//		try {
//			accToken = accountRepository.findByToken(request.getHeader("Authorization").split(" ")[1]);
//		} catch (NullPointerException e) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//
//		if (accToken == null || accToken.getRole().getID() != 3) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//		List<Shop> shops = repository.findAll();
//		double sum = 0;
//		for (Shop s : shops) {
//			for (Transaction j : s.getTransaction()) {
//				if (j.getTime().getMonth() + 1 == month) {
//					sum = sum + j.getTotal();
//				}
//			}
//			
//		}
//		if (sum == 0) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
//							responseMessage.get("notFoundDataMonth") + month + "!", ""));
//		}
//		if (month < 1 || month > 12) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("inputMonthFail"), ""));
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("totalRevenue"), Math.round(sum * 100.0) /100.0));
//	}
//
//	public ResponseEntity<ResponseObject> getTotalRevenueByYear(HttpServletRequest request, int year) {
//		String token = "";
//		try {
//			token = request.getHeader("Authorization").split(" ")[1];
//		} catch (NullPointerException e) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//
//		Account acc = accountRepository.findByToken(token);
//
//		if (acc == null || acc.getRole().getID() != 3) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		}
//
//		List<Shop> shops = repository.findAll();
//		
//		List<Map<String, Object>> data = new ArrayList<>();
//
//		List<String> months = new ArrayList<>();
//		months.add("Jan");
//		months.add("Feb");
//		months.add("Mar");
//		months.add("Apr");
//		months.add("May");
//		months.add("Jun");
//		months.add("Jul");
//		months.add("Aug");
//		months.add("Sep");
//		months.add("Oct");
//		months.add("Nov");
//		months.add("Dec");
//
//		for (int month = 1; month <= 12; month++) {
//			double sum = 0;
//			for (Shop shop : shops) {
//				for (Transaction tran : shop.getTransaction()) {
//					if (tran.getTime().getMonth() + 1 == month && tran.getTime().getYear() + 1900 == year) {
//						sum += tran.getTotal();
//					}
//				}
//			}
//			Map<String, Object> ob = new HashMap<>();
//		    ob.put("time", months.get(month - 1));
//		    ob.put("value", Math.round(sum * 100.0) / 100.0);
//		    data.add(ob);
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getTotalRevenueByYearSuccess"), data));
//	}
}
