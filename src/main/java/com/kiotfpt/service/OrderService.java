package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.AccessibilityItemRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.CreateOrderRequest;
import com.kiotfpt.request.ItemRequest;
import com.kiotfpt.request.SectionRequest;
import com.kiotfpt.request.StatusRequest;
import com.kiotfpt.response.OrderResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StatusRepository statusRepository;

//	@Autowired
//	private ProductRepository productRepository;
	
	@Autowired
	private AccessibilityItemRepository itemRepository;

//	@Autowired
//	private JavaMailSender mailSender;

	public String randomNumber;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getOrderByAccountID(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Order> orders = repository.findAllByAccount(acc.get());
			if (!orders.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Orders found", orders));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Orders do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> getOrderByShopID(int shop_id, int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findById(shop_id);
		if (!shopOptional.isEmpty()) {
			Pageable pageable = PageRequest.of(page - 1, amount);
			Page<Order> orderPage = repository.findAllByShop(shopOptional.get(), pageable);

			if (orderPage.hasContent()) {
				List<OrderResponse> list = new ArrayList<>();
				for (Order order : orderPage.getContent()) {
					OrderResponse response = new OrderResponse(order);
					list.add(response);
				}
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Orders found", list));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "No orders found for the given shop ID", ""));
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop not found", ""));
		}
	}

	public ResponseEntity<ResponseObject> getCurrentOrders(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Order> transactions = repository.findAllByAccount(acc.get());
			if (!transactions.isEmpty()) {
				List<Order> returnListOrders = new ArrayList<>(); // List to store products with status not 2, 3, or
																	// 4
				// Iterate through foundProduct list to check status
				for (Order transaction : transactions) {
					int status = transaction.getSection().getStatus().getId();
					// Check if status is not 2, 3, or 4
					if (status == 1 || status == 2 || status == 3 || status == 4) {
						returnListOrders.add(transaction);
					}
				}
				if (!returnListOrders.isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdSuccess"), returnListOrders));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdFail"), ""));
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							responseMessage.get("transactionNotFound"), transactions));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> createOrder(CreateOrderRequest input) {
		// Assuming each section is one order
		for (SectionRequest sectionRequest : input.getSections()) {
			Section section = sectionRepository.findById(sectionRequest.getSection_id()).orElse(null);
			if (section != null) {
				Shop shop = section.getShop();
				Account account = accountRepository.findById(input.getAccountId()).orElse(null);
				Status status = statusRepository.findByValue("Pending").orElse(null);

				Order order = new Order();
				order.setTimeInit(new Date());
				order.setDesc("Desciption");
				order.setTotal(calculateOrderTotal(sectionRequest.getItems())); // Implement this method

				order.setSection(section);
				order.setShop(shop);
				order.setAccount(account);
				order.setStatus(status);

				repository.save(order);

				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], "Order created successfully", order));
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Section not found", null));
	}

    private float calculateOrderTotal(List<ItemRequest> items) {
        float total = 0.0f;
        for (ItemRequest itemRequest : items) {
            AccessibilityItem item = itemRepository.findById(itemRequest.getItem_id()).orElse(null);
            if (item != null) {
//                double productPrice = item.getProduct().getProduct_price();
                total += itemRequest.getItem_quantity() * item.getVariant().getPrice();
            } else {
                // Handle the case where the item is not found
                // You may throw an exception, log an error, or handle it based on your requirements
                // For now, let's just log an error
                System.err.println("Item not found for ID: " + itemRequest.getItem_id());
            }
        }
        return total;
    }

	public ResponseEntity<ResponseObject> updateOrderStatus(int id, StatusRequest status) {
		Optional<Order> order = repository.findById(id);
		if (!order.isEmpty()) {
			Optional<Status> newStat = statusRepository.findByValue(status.getValue());
			if (!newStat.isEmpty()) {
				order.get().setStatus(newStat.get());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Update order sucessfully", repository.save(order.get())));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status does not exist", ""));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order does not exist", ""));
	}

//    private void createTransaction(Order order) {
//        Transaction transaction = new Transaction();
//        transaction.setTransaction_time_init(order.getOrder_time_init());
//        transaction.setTransaction_time_complete(new Date());
//        transaction.setTransaction_desc(order.getOrder_desc());
//        transaction.setTransaction_total(order.getOrder_total());
//        transaction.setShop(order.getShop());
//        transaction.setAccount(order.getAccount());
//        transactionRepository.save(transaction);
//    }

//	public ResponseEntity<ResponseObject> updateOrder(Order newOrder) {
//		Order updateOrder = repository.findById(newOrder.getID()).map(transaction -> {
//			transaction.setTotal(newOrder.getTotal());
//			transaction.setDesciption(newOrder.getDesciption());
//			transaction.setStatus(newOrder.getStatus());
//			transaction.setTime(newOrder.getTime());
//			return repository.save(transaction);
//		}).orElseGet(() -> {
//			return null;
//		});
//		if (updateOrder == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("transactionNotFound"), ""));
//		} else {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(false, HttpStatus.OK.toString().split(" ")[0],
//							responseMessage.get("updateOrderSuccess"), updateOrder));
//		}
//	}
//

//
//	public ResponseEntity<ResponseObject> checkOtpPayment(Map<String, String> obj, HttpServletRequest request) {
//		Optional<Order> transaction = repository.findById(Integer.parseInt(obj.get("transaction_id")));
//		if (transaction.isPresent()) {
//			Optional<Account> acc = accountRepository.findById((transaction.get().getAccount().getID()));
//			if (acc.isPresent()) {
//				String checkToken = new TokenUtils().checkMatch(acc.get().getID(), request, accountRepository);
//				switch (checkToken) {
//				case "ok":
//					if (transaction.get().getAccount() == null) {
//						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//								.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//										responseMessage.get("accountNotFound"), ""));
//					} else {
//						if (transaction.get().getAccount().getID() != acc.get().getID()) {
//							return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//									.body(new ResponseObject(false, HttpStatus.UNAUTHORIZED.toString().split(" ")[0],
//											responseMessage.get("unauthorized"), ""));
//						} else {
//							if (obj.get("otpCode").isEmpty()) {
//								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//										.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//												responseMessage.get("OtpEmpty"), ""));
//							} else if (obj.get("otpCode").equals(randomNumber)) {
//								if (acc.get().getWallet() > transaction.get().getTotal()) {
//									acc.get().setWallet(acc.get().getWallet() - transaction.get().getTotal());
//									accountRepository.save(acc.get());
//									transaction.get().setStatus(1);
//									repository.save(transaction.get());
//									return ResponseEntity.status(HttpStatus.OK)
//											.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//													responseMessage.get("paymentSuccess"), acc.get().getWallet()));
//								} else
//									return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//											.body(new ResponseObject(false,
//													HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//													responseMessage.get("notEnoughMoney"), ""));
//							} else
//								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//										.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//												responseMessage.get("OtpWrong"), ""));
//						}
//					}
//				case "unauthorized":
//					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//							HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//				case "empty":
//					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//							HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//				default:
//					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//							HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//				}
//			} else
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), ""));
//
//		} else
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
//							responseMessage.get("notFoundOrder") + obj.get("transaction_id"), ""));
//	}
}
