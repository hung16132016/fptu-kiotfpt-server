package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Address;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.ShopRequest;
import com.kiotfpt.request.StatusRequest;
import com.kiotfpt.response.ShopResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ValidationHelper;

@Service
public class ShopService {
	@Autowired
	private ShopRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StatusRepository statusRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getShopByID(int id) {
		Optional<Shop> shop = repository.findById(id);
		if (!shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shop.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> getAllShop(int page, int amount) {
		Pageable pageable = PageRequest.of(page - 1, amount);
		Page<Shop> shopPage = repository.findAll(pageable);
		List<Shop> shops = shopPage.getContent();

		if (!shops.isEmpty()) {
			List<ShopResponse> list = new ArrayList<ShopResponse>();

			for (Shop shop : shops) {

				if (shop.getAddress() == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
									"Address cannot be empty for shop with ID: " + shop.getId(), ""));
				}
				ShopResponse response = new ShopResponse(shop);

				list.add(response);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Shops found", list));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> createShop(ShopRequest shopRequest) {

		if (shopRequest == null || shopRequest.getName() == null || shopRequest.getName().isEmpty()
				|| shopRequest.getEmail() == null || shopRequest.getEmail().isEmpty() || shopRequest.getPhone() == null
				|| shopRequest.getPhone().isEmpty() || shopRequest.getThumbnail() == null
				|| shopRequest.getThumbnail().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.OK.toString().split(" ")[0], "Error in input", ""));
		}

		ValidationHelper help = new ValidationHelper();
		if (help.isValidEmail(shopRequest.getEmail()) == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Mail is not valid", ""));
		}

		if (help.isVietnamPhoneNumber(shopRequest.getPhone()) == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Phone is not valid", ""));
		}

		Optional<Account> account = accountRepository.findById(shopRequest.getAccount_id());
		if (account.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Account not found", ""));
		}

		AddressService service = new AddressService();
		
		ResponseEntity<ResponseObject> response = service.createAddress(shopRequest.getAddress());
		
		if (response.getBody().getResult() == false) {
			return response;
		}
		
	    Address address = (Address) response.getBody().getData();
	    
		Shop shop = new Shop(shopRequest, account.get(), address);
		repository.save(shop);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Create shop successfully. Here is your account", shop));
	}

	public ResponseEntity<ResponseObject> updateShop(int id, ShopRequest shop) {
		Optional<Shop> foundshop = repository.findById(id);
		if (foundshop.isPresent()) {
			ValidationHelper help = new ValidationHelper();
			if (help.isValidEmail(shop.getEmail()) == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Mail is not valid", ""));
			}

			if (help.isVietnamPhoneNumber(shop.getPhone()) == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Phone is not valid", ""));
			}

			foundshop.get().setEmail(shop.getEmail());
			foundshop.get().setName(shop.getName());
			foundshop.get().setPhone(shop.getPhone());
			foundshop.get().setThumbnail(shop.getThumbnail());

			repository.save(foundshop.get());

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("updateShopSuccess"), shop));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> banShop(int shopId, StatusRequest request) {
		Optional<Shop> optionalShop = repository.findById(shopId);
		if (!optionalShop.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop with id: " + shopId + " not found", null));
		}

		Shop shop = optionalShop.get();
		Account account = shop.getAccount();
		if (account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							"No account associated with shop id: " + shopId, null));
		}

		// Determine the new status value
		String statusValue = request.getValue().equalsIgnoreCase("true") ? "inactive" : "active";

		// Fetch the corresponding status from the database
		Optional<Status> statusOptional = statusRepository.findByValue(statusValue);
		if (!statusOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							statusValue + " status not found in database", null));
		}

		// Update the account status
		account.setStatus(statusOptional.get());
		accountRepository.save(account);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Change status successfully", null));
	}

	public ResponseEntity<ResponseObject> getTop10ShopsByTransactions() {
		List<Shop> topShops = repository.findTop10ByTransactions();
		List<ShopResponse> shopResponses = topShops.stream().map(shop -> new ShopResponse(shop))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
						"Top 10 shops by transactions retrieved successfully", shopResponses));
	}
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
