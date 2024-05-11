//package com.kiotfpt.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.kiotfpt.model.Account;
//import com.kiotfpt.model.Address;
//import com.kiotfpt.model.District;
//import com.kiotfpt.model.Follow;
//import com.kiotfpt.model.Province;
//import com.kiotfpt.model.ResponseObject;
//import com.kiotfpt.model.Shop;
//import com.kiotfpt.repository.AccountRepository;
//import com.kiotfpt.repository.ShopRepository;
//import com.kiotfpt.request.AddressRequest;
//import com.kiotfpt.request.DistrictRequest;
//import com.kiotfpt.request.ProvinceRequest;
//import com.kiotfpt.request.ShopRequest;
//import com.kiotfpt.response.AddressResponse;
//import com.kiotfpt.response.DistrictResponse;
//import com.kiotfpt.response.ProvinceResponse;
//import com.kiotfpt.response.ShopResponse;
//import com.kiotfpt.utils.JsonReader;
//import com.kiotfpt.utils.MD5;
//
//
//@Service
//public class ShopService {
//	@Autowired
//	private ShopRepository repository;
//
//	@Autowired
//	private AccountRepository accountRepository;
//
//	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();
//
//	public ResponseEntity<ResponseObject> getShopByID(int id) {
//		Optional<Shop> shop = repository.findById(id);
//		if (!shop.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shop.get()));
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
//	}
//
//	public ResponseEntity<ResponseObject> getAllShop(int page, int amount) {
//		Pageable pageable = PageRequest.of(page - 1, amount);
//		Page<Shop> shopPage = repository.findAll(pageable);
//		List<Shop> shops = shopPage.getContent();
//		
//		Follow fo = new Follow();
//		
//		if (!shops.isEmpty()) {
//			List<ShopResponse> list = new ArrayList<ShopResponse>();
//
//			for (Shop shop : shops) {
//
//				if (shop.getAddress() == null) {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//							.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//									"Address cannot be empty for shop with ID: " + shop.getShop_id(), ""));
//				}
//
//				DistrictResponse district = new DistrictResponse(shop.getAddress().getDistrict().getDistrict_id(),
//						shop.getAddress().getDistrict().getDistrict_value());
//				ProvinceResponse province = new ProvinceResponse(shop.getAddress().getProvince().getProvince_id(),
//						shop.getAddress().getProvince().getProvince_value());
//				AddressResponse address = new AddressResponse(shop.getAddress().getAddress_id(),
//						shop.getAddress().getAddress_value(), district, province);
//				ShopResponse response = new ShopResponse(shop.getShop_id(), shop.getShop_name(), shop.getShop_email(),
//						shop.getShop_phone(), shop.getShop_thumbnail(), address);
//
//				list.add(response);
//			}
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Shops found", list));
//		}
//
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
//	}
//
//	public ResponseEntity<ResponseObject> createShop(ShopRequest shopRequest) {
//
//		if (shopRequest == null || shopRequest.getShop_name() == null || shopRequest.getShop_name().isEmpty()
//				|| shopRequest.getShop_email() == null || shopRequest.getShop_email().isEmpty()
//				|| shopRequest.getShop_phone() == null || shopRequest.getShop_phone().isEmpty()
//				|| shopRequest.getShop_thumbnail() == null || shopRequest.getShop_thumbnail().isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.OK.toString().split(" ")[0], "Error in input", "Invalid input data"));
//		}
//		// Generate random password
//		String randomPassword = generateRandomPassword(10);
//
//		// Hash the password
//		String hashedPassword = MD5.generateMD5Hash(randomPassword);
//
//		// Create account with email as username and hashed password
//		Account account = new Account();
//		account.setAccount_username(shopRequest.getShop_email());
//		account.setAccount_password(hashedPassword);
//
//		// Create shop
//		Shop shop = new Shop();
//		shop.setShop_name(shopRequest.getShop_name());
//		shop.setShop_email(shopRequest.getShop_email());
//		shop.setShop_phone(shopRequest.getShop_phone());
//		shop.setShop_thumbnail(shopRequest.getShop_thumbnail());
//
//		// Associate account with shop
//		shop.setAccount(account);
//
//		// Save account and shop
//		accountRepository.save(account);
//		repository.save(shop);
//
//		return ResponseEntity.status(HttpStatus.OK).body(
//				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Create shop successfully. Here is your account", account));
//	}
//
//	// Method to generate random password
//	private String generateRandomPassword(int length) {
//		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@";
//		StringBuilder randomPassword = new StringBuilder();
//		Random random = new Random();
//		for (int i = 0; i < length; i++) {
//			randomPassword.append(characters.charAt(random.nextInt(characters.length())));
//		}
//		return randomPassword.toString();
//	}
//
//	public ResponseEntity<ResponseObject> updateShop(int id, ShopRequest shop) {
//		Optional<Shop> foundshop = repository.findById(id);
//		if (foundshop.isPresent()) {
//			AddressRequest new_address = shop.getAddress();
//			DistrictRequest new_district = new_address.getDistrict();
//			ProvinceRequest new_province = new_address.getProvince();
//
//			District district = new District(new_district.getDistrict_id(), new_district.getDistrict_value());
//			Province province = new Province(new_province.getProvince_id(), new_province.getProvince_value());
//			Address address = new Address(new_address.getAddress_id(), new_address.getAddress_value(), district,
//					province, foundshop.get().getAddress().getProfile());
//
//			foundshop.get().setAddress(address);
//			foundshop.get().setShop_email(shop.getShop_email());
//			foundshop.get().setShop_name(shop.getShop_name());
//			foundshop.get().setShop_phone(shop.getShop_phone());
//			foundshop.get().setShop_thumbnail(shop.getShop_thumbnail());
//
//			repository.save(foundshop.get());
//
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
//					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("updateShopSuccess"), shop));
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
//	}
////
////	public ResponseEntity<ResponseObject> getShopByAccountID(HttpServletRequest request, int id) {
////		Optional<Shop> shop = repository.findByAccountID(id);
////		if (shop.isPresent()) {
////			String token = "";
////			try {
////				token = request.getHeader("Authorization").split(" ")[1];
////			} catch (NullPointerException e) {
////				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////						HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////			}
////
////			if (token.equals(shop.get().getAccount().getToken())) {
////				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
////						HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getShopByIdSuccess"), shop.get()));
////			} else {
////				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////						HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////			}
////		}
////		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
////				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("getShopByAccountIdFail"), ""));
////	}
////
////	public ResponseEntity<ResponseObject> getAllShopRevenueByTime(HttpServletRequest request, int month) {
////		Account accToken = null;
////		try {
////			accToken = accountRepository.findByToken(request.getHeader("Authorization").split(" ")[1]);
////		} catch (NullPointerException e) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////
////		if (accToken == null || accToken.getRole().getID() != 3) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////		List<Shop> shops = repository.findAll();
////		double sum = 0;
////		List<Map<String, Object>> shopDataList = new ArrayList<>();
////		for (Shop s : shops) {
////			Map<String, Object> shopData = new HashMap<>();
////			for (Transaction j : s.getTransaction()) {
////				if (j.getTime().getMonth() + 1 == month) {
////					sum = sum + j.getTotal();
////				}
////			}
////			if(sum == 0) {
////				continue;
////			}
////			shopData.put("shop", s);
////			shopData.put("revenue", sum);
////			sum = 0;
////			shopDataList.add(shopData);
////		}
////		if (shopDataList.isEmpty()) {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND)
////					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
////							responseMessage.get("notFoundDataMonth") + month + "!", ""));
////		}
////
////		if (month < 1 || month > 12) {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
////					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("inputMonthFail"), ""));
////		}
////		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
////				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shopDataList));
////	}
////
////	public ResponseEntity<ResponseObject> getTotalRevenueByTime(HttpServletRequest request, int month) {
////		Account accToken = null;
////		try {
////			accToken = accountRepository.findByToken(request.getHeader("Authorization").split(" ")[1]);
////		} catch (NullPointerException e) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////
////		if (accToken == null || accToken.getRole().getID() != 3) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////		List<Shop> shops = repository.findAll();
////		double sum = 0;
////		for (Shop s : shops) {
////			for (Transaction j : s.getTransaction()) {
////				if (j.getTime().getMonth() + 1 == month) {
////					sum = sum + j.getTotal();
////				}
////			}
////			
////		}
////		if (sum == 0) {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND)
////					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
////							responseMessage.get("notFoundDataMonth") + month + "!", ""));
////		}
////		if (month < 1 || month > 12) {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
////					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("inputMonthFail"), ""));
////		}
////		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
////				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("totalRevenue"), Math.round(sum * 100.0) /100.0));
////	}
////
////	public ResponseEntity<ResponseObject> getTotalRevenueByYear(HttpServletRequest request, int year) {
////		String token = "";
////		try {
////			token = request.getHeader("Authorization").split(" ")[1];
////		} catch (NullPointerException e) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////
////		Account acc = accountRepository.findByToken(token);
////
////		if (acc == null || acc.getRole().getID() != 3) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
////					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
////		}
////
////		List<Shop> shops = repository.findAll();
////		
////		List<Map<String, Object>> data = new ArrayList<>();
////
////		List<String> months = new ArrayList<>();
////		months.add("Jan");
////		months.add("Feb");
////		months.add("Mar");
////		months.add("Apr");
////		months.add("May");
////		months.add("Jun");
////		months.add("Jul");
////		months.add("Aug");
////		months.add("Sep");
////		months.add("Oct");
////		months.add("Nov");
////		months.add("Dec");
////
////		for (int month = 1; month <= 12; month++) {
////			double sum = 0;
////			for (Shop shop : shops) {
////				for (Transaction tran : shop.getTransaction()) {
////					if (tran.getTime().getMonth() + 1 == month && tran.getTime().getYear() + 1900 == year) {
////						sum += tran.getTotal();
////					}
////				}
////			}
////			Map<String, Object> ob = new HashMap<>();
////		    ob.put("time", months.get(month - 1));
////		    ob.put("value", Math.round(sum * 100.0) / 100.0);
////		    data.add(ob);
////		}
////
////		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
////				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("getTotalRevenueByYearSuccess"), data));
////	}
//}
