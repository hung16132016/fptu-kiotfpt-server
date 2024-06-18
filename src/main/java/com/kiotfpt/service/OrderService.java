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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Notify;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Transaction;
import com.kiotfpt.repository.AccessibilityItemRepository;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.NotifyRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.TransactionRepository;
import com.kiotfpt.request.CreateOrderRequest;
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

	@Autowired
	private AccessibilityItemRepository itemRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private NotifyRepository notifyRepository;

	@Autowired
	private AccountProfileRepository profileRepository;

	public String randomNumber;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getOrderByAccountID(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Order> orders = repository.findAllByAccount(acc.get());
			if (!orders.isEmpty()) {
				List<OrderResponse> orderResponses = new ArrayList<>();
				for (Order order : orders) {
					orderResponses.add(new OrderResponse(order));
				}
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], "Orders found", orderResponses));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Orders do not exist", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Account not found", ""));
	}

	public ResponseEntity<ResponseObject> getOrderByShopID(int shop_id, int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findById(shop_id);
		if (!shopOptional.isEmpty()) {
			Pageable pageable = PageRequest.of(page - 1, amount);
			Page<Order> orderPage = repository.findAllByShop(shopOptional.get(), pageable);

			if (orderPage.hasContent()) {
				List<OrderResponse> list = new ArrayList<>();
				for (Order order : orderPage.getContent()) {
					Optional<AccountProfile> profile = profileRepository.findByAccount(order.getAccount());
					if (profile.isEmpty()) {
						return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
										"No profile found for order with ID " + order.getId(), ""));
					}
					
					OrderResponse response = new OrderResponse(order, profile.get());
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
			List<Order> orders = repository.findAllByAccount(acc.get());
			if (!orders.isEmpty()) {
				List<Order> returnListOrders = new ArrayList<>(); // List to store products with status not 2, 3, or
																	// 4
				// Iterate through foundProduct list to check status
				for (Order order : orders) {
					int status = order.getSection().getStatus().getId();
					// Check if status is not 2, 3, or 4
					if (status == 1 || status == 2 || status == 3 || status == 4) {
						returnListOrders.add(order);
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
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("transactionNotFound"), orders));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> createOrder(CreateOrderRequest input) {
		try {
			for (SectionRequest sectionRequest : input.getSections()) {

				// Validate Section existence
				Optional<Section> optionalSection = sectionRepository.findById(sectionRequest.getSection_id());
				if (optionalSection.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
							HttpStatus.NOT_FOUND.toString().split(" ")[0], "Section not found", null));
				}
				Section section = optionalSection.get();

				// Validate Shop existence
				Shop shop = section.getShop();
				if (shop == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
							HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop not found", null));
				}

				// Validate Account existence
				Optional<Account> optionalAccount = accountRepository.findById(input.getAccountId());
				if (optionalAccount.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
							HttpStatus.NOT_FOUND.toString().split(" ")[0], "Account not found", null));
				}
				Account account = optionalAccount.get();

				// Fetch the 'Pending' Status
				Optional<Status> optionalStatus = statusRepository.findByValue("pending");
				Optional<Status> processStatus = statusRepository.findByValue("processing");
				if (optionalStatus.isEmpty() || processStatus.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
							HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status not found", null));
				}
				Status status = optionalStatus.get();

				// Fetch the accessibility items of the section
				List<AccessibilityItem> accessibilityItems = itemRepository.findBySection(section);

				if (sectionRequest.getItem_id().size() == accessibilityItems.size()) {
					// Create and populate Order entity
					Order order = new Order(sectionRequest, calculateOrderTotal(sectionRequest.getItem_id()), section,
							shop, account, status);

					section.setStatus(processStatus.get());
					for (Integer itemId : sectionRequest.getItem_id()) {
						AccessibilityItem item = itemRepository.findById(itemId).orElse(null);
						if (item != null) {
							item.setStatus(processStatus.get());
							itemRepository.save(item);
						}
					}

					// Save the Order entity
					repository.save(order);
					notifyRepository.save(new Notify(order, account, "pending"));
				} else if (sectionRequest.getItem_id().size() < accessibilityItems.size()) {
					// Create a new section
					Section newSection = new Section();
					newSection.setShop(shop);
					newSection.setCart(section.getCart());
					newSection.setStatus(processStatus.get());
					newSection.setTotal(calculateOrderTotal(sectionRequest.getItem_id()));

					// Save the new section
					Section savedSection = sectionRepository.save(newSection);

					// Move the items to the new section
					for (Integer itemId : sectionRequest.getItem_id()) {
						AccessibilityItem item = itemRepository.findById(itemId).orElse(null);
						if (item != null) {
							item.setSection(savedSection);
							item.setStatus(processStatus.get());
							itemRepository.save(item);
						}
					}

					// Create and populate Order entity for the new section
					Order order = new Order(sectionRequest, calculateOrderTotal(sectionRequest.getItem_id()),
							savedSection, shop, account, status);

					// Save the Order entity
					repository.save(order);
					notifyRepository.save(new Notify(order, account, "pending"));

				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
									"Invalid number of items in section request", null));
				}

			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Order created successfully", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"An error occurred while creating the order", null));
		}
	}

	private float calculateOrderTotal(List<Integer> items) {
		float total = 0.0f;
		for (Integer items_id : items) {
			AccessibilityItem item = itemRepository.findById(items_id).orElse(null);
			if (item != null) {
				total += item.getQuantity() * item.getVariant().getPrice();
			} else {
				System.err.println("Item not found for ID: " + items_id);
			}
		}
		return total;
	}

	public ResponseEntity<ResponseObject> updateOrderStatus(int id, StatusRequest status)
			throws JsonProcessingException {
		Optional<Order> order = repository.findById(id);
		if (!order.isEmpty()) {
			Optional<Status> newStat = statusRepository.findByValue(status.getValue());
			if (!newStat.isEmpty()) {
				if (newStat.get().getValue().equalsIgnoreCase("completed")) {

					order.get().setTimeComplete(new Date());
					order.get().setStatus(newStat.get());
					order.get().getSection().setStatus(newStat.get());

					for (AccessibilityItem item : order.get().getSection().getItems()) {
						item.setStatus(newStat.get());
						itemRepository.save(item);
					}
					repository.save(order.get());

					Transaction trans = new Transaction(order.get());

					transactionRepository.save(trans);

					notifyRepository.save(new Notify(order.get(), order.get().getAccount(), "completed"));
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
							HttpStatus.OK.toString().split(" ")[0], "Create transaction successfully", trans));

				} else {
					order.get().setStatus(newStat.get());
					order.get().getSection().setStatus(newStat.get());

					for (AccessibilityItem item : order.get().getSection().getItems()) {
						item.setStatus(newStat.get());
						itemRepository.save(item);
					}
					notifyRepository.save(new Notify(order.get(), order.get().getAccount(), status.getValue()));

					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									"Update order sucessfully", repository.save(order.get())));
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status does not exist", ""));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order does not exist", ""));
	}

}
