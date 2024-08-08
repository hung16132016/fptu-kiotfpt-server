package com.kiotfpt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Address;
import com.kiotfpt.model.Notify;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Transaction;
import com.kiotfpt.model.Variant;
import com.kiotfpt.model.Voucher;
import com.kiotfpt.repository.AccessibilityItemRepository;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.AddressRepository;
import com.kiotfpt.repository.NotifyRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.TransactionRepository;
import com.kiotfpt.repository.VariantRepository;
import com.kiotfpt.repository.VoucherRepository;
import com.kiotfpt.request.CreateOrderRequest;
import com.kiotfpt.request.DateRequest;
import com.kiotfpt.request.SectionRequest;
import com.kiotfpt.response.OrderResponse;
import com.kiotfpt.response.OrderStatisResponse;
import com.kiotfpt.utils.DateUtil;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private VoucherRepository voucherRepository;

	@Autowired
	private AccountProfileRepository profileRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private VariantRepository variantRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TokenUtils tokenUtils;

	public String randomNumber;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getOrderByAccountID() {
		Account acc = tokenUtils.getAccount();

		List<Order> orders = repository.findAllByAccount(acc);
		if (!orders.isEmpty()) {
			List<OrderResponse> orderResponses = new ArrayList<>();
			for (Order order : orders) {
				orderResponses.add(new OrderResponse(order));
			}
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Orders found", orderResponses));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], "Orders do not exist", new int[0]));

	}

	public ResponseEntity<ResponseObject> getOrderByShopID(int page, int amount) {
		Optional<Shop> shopOptional = shopRepository.findByAccount(tokenUtils.getAccount());
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

			Account account = tokenUtils.getAccount();

			// Validate Address existence
			Optional<Address> optionalAddress = addressRepository.findById(input.getAddress_id());
			if (optionalAddress.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Address not found", null));
			}
			if (optionalAddress.get().getProfile().getAccount().getId() != account.getId()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Address not from same account", null));
			}
			Address address = optionalAddress.get();

			// Fetch the 'Pending' and 'Processing' Status
			Optional<Status> optionalStatus = statusRepository.findByValue("paying");
			Optional<Status> activeStatus = statusRepository.findByValue("active");
			Optional<Status> processStatus = statusRepository.findByValue("processing");
			if (optionalStatus.isEmpty() || processStatus.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Status not found");
			}
			Status status = optionalStatus.get();

			for (SectionRequest sectionRequest : input.getSections()) {
				// Validate Section existence
				Optional<Section> optionalSection = sectionRepository.findById(sectionRequest.getSection_id());
				if (optionalSection.isEmpty()) {
					return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Section not found");
				}
				Section section = optionalSection.get();

				// Validate Shop existence
				Shop shop = section.getShop();
				if (shop == null) {
					return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Shop not found");
				}

				// Validate Voucher for the section
				Voucher voucher = null;
				if (sectionRequest.getVoucher_id() != 0) {
					Optional<Voucher> optionalVoucher = voucherRepository.findById(sectionRequest.getVoucher_id());
					if (optionalVoucher.isEmpty() || optionalVoucher.get().getShop().getId() != shop.getId()
							|| optionalVoucher.get().getStatus().getValue().equalsIgnoreCase("inactive")) {
						return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
								"Invalid voucher for the shop");
					}

					voucher = optionalVoucher.get();
				} else {
					voucher = new Voucher(0, 0, null, activeStatus.get()); // Default voucher with zero value
				}

				// Fetch the accessibility items of the section
				List<AccessibilityItem> accessibilityItems = itemRepository.findBySection(section);

				float orderTotal = calculateOrderTotal(sectionRequest.getItem_id());

				// Apply voucher discount if available
				if (voucher != null && voucher.getValue() > 0) {
					orderTotal = orderTotal - (orderTotal * voucher.getValue() / 100);
					voucher.setStatus(statusRepository.findByValue("inactive").get());
					voucherRepository.save(voucher);
				}

				if (sectionRequest.getItem_id().size() == accessibilityItems.size()) {
					// Create and populate Order entity
					Order order = new Order(input.getOrder_id(),sectionRequest, orderTotal, section, shop, account, status, address);

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
					newSection.setTotal(orderTotal);

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
					Order order = new Order(input.getOrder_id(),sectionRequest, orderTotal, savedSection, shop, account, status, address);

					// Save the Order entity
					repository.save(order);
					notifyRepository.save(new Notify(order, account, "pending"));

				} else {
					return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
							"Invalid number of items in section request");
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

	public ResponseEntity<ResponseObject> updateOrderStatus(int id, String status)
			throws JsonProcessingException, MessagingException {
		Optional<Order> orderOpt = repository.findById(id);
		if (orderOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order does not exist", ""));
		}

		Order order = orderOpt.get();

		Optional<Status> newStatOpt = statusRepository.findByValue(status);
		if (newStatOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status does not exist", ""));
		}

		Status newStat = newStatOpt.get();

		if (newStat.getValue().equalsIgnoreCase("completed")) {
			order.setTimeComplete(LocalDateTime.now());
			order.setStatus(newStat);
			order.getSection().setStatus(newStat);

			for (AccessibilityItem item : order.getSection().getItems()) {
				item.setStatus(newStat);
				itemRepository.save(item);
			}
			repository.save(order);

			Transaction trans = new Transaction(order);
			transactionRepository.save(trans);

			notifyRepository.save(new Notify(order, order.getAccount(), "completed"));
			Optional<AccountProfile> profile = profileRepository.findByAccount(order.getAccount());

			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(new InternetAddress("kiotfpt.help@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, profile.get().getEmail());
			message.setSubject("[Order] Your Order is completed");
			String htmlContent = "This email is for confirm that your order with id: " + order.getId()
					+ " have completed with total of " + order.getTotal() + ".";
			message.setContent(htmlContent, "text/html; charset=utf-8");
			mailSender.send(message);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Create transaction successfully", trans));

		} else if (order.getStatus().getValue().equals("accepted") && newStat.getValue().equalsIgnoreCase("delivering")
				&& tokenUtils.checkMatch("shop")) {
			order.setStatus(newStat);
			order.getSection().setStatus(newStat);

			for (AccessibilityItem item : order.getSection().getItems()) {
				item.setStatus(newStat);

				Variant variant = item.getVariant();
				if (variant != null) {
					int newQuantity = variant.getQuantity() - item.getQuantity();
					if (newQuantity < 0) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
										"Insufficient quantity in variant", null));
					}
					variant.setQuantity(newQuantity);
					variantRepository.save(variant);
				}

				itemRepository.save(item);
			}

			repository.save(order);
			notifyRepository.save(new Notify(order, order.getAccount(), "delivering"));

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Order status updated to delivering successfully", order));

		} else if (!order.getStatus().getValue().equals("pending") && !order.getStatus().getValue().equals("accepted")
				&& newStat.getValue().equalsIgnoreCase("cancel")) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "You can not cancel this order");

		} else {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "You can not update this order");
		}
	}

	public ResponseEntity<ResponseObject> updateOrderStatusPay(int id) {
		Optional<Order> orderOpt = repository.findById(id);
		if (orderOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order does not exist", ""));
		}

		Order order = orderOpt.get();

		if (!order.getStatus().getValue().equalsIgnoreCase("paying")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Status is not valid", ""));
		}

		Optional<Status> newStatOpt = statusRepository.findByValue("pending");
		if (newStatOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status does not exist", ""));
		}

		Status newStat = newStatOpt.get();

		order.setStatus(newStat);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Change status order successfully", ""));

	}

	public ResponseEntity<ResponseObject> filterOrdersByTime(DateRequest filterRequest) {
		try {

			Date startLocalDateTime = DateUtil.calculateStartDate(filterRequest),
					endLocalDateTime = DateUtil.calculateEndDate(filterRequest);

			LocalDateTime startDate = DateUtil.toLocalDateTime(startLocalDateTime);
			LocalDateTime endDate = DateUtil.toLocalDateTime(endLocalDateTime);

			List<Order> orders = null;
			if (tokenUtils.checkMatch("admin")) {
				orders = repository.findByTimeInitBetweenAndStatusIn(startDate, endDate);
			} else if (tokenUtils.checkMatch("shop")) {

				Optional<Shop> shop = shopRepository.findByAccount(tokenUtils.getAccount());
				if (shop.isEmpty()) {
					return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
							"No shop found with the current account");
				}

				orders = repository.findByTimeInitBetweenAndShopIdAndStatusIn(startDate, endDate, shop.get().getId());
			}

			if (orders.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
								"No completed orders found for the given date", null));
			}

			List<OrderStatisResponse> responseList = orders.stream()
					.map(order -> new OrderStatisResponse(order.getId(), order.getTotal(), order.getTimeInit()))
					.collect(Collectors.toList());

			float totalOfAllOrders = orders.stream().map(Order::getTotal).reduce(0.0f, Float::sum);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Completed orders found",
							new OrderFilterResult(responseList, totalOfAllOrders, responseList.size())));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"An error occurred while filtering completed orders", null));
		}
	}

	public ResponseEntity<ResponseObject> revenue(DateRequest filterRequest) {

		Date startLocalDateTime = DateUtil.calculateStartDate(filterRequest),
				endLocalDateTime = DateUtil.calculateEndDate(filterRequest);

		LocalDateTime startDate = DateUtil.toLocalDateTime(startLocalDateTime);
		LocalDateTime endDate = DateUtil.toLocalDateTime(endLocalDateTime);

		List<Order> orders = null;
		if (tokenUtils.checkMatch("admin")) {
			orders = repository.findByTimeCompleteBetweenAndStatusValue(startDate, endDate, "completed");
		} else if (tokenUtils.checkMatch("shop")) {

			Optional<Shop> shop = shopRepository.findByAccount(tokenUtils.getAccount());
			if (shop.isEmpty()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
						"No shop found with the current account");
			}

			orders = repository.findByTimeCompleteBetweenAndShopIdAndStatusValue(startDate, endDate, shop.get().getId(),
					"completed");
		}

		if (orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
							"No completed orders found for the given date", null));
		}

		List<OrderStatisResponse> responseList = orders.stream()
				.map(order -> new OrderStatisResponse(order.getId(), order.getTotal(), order.getTimeComplete()))
				.collect(Collectors.toList());

		float totalOfAllOrders = orders.stream().map(Order::getTotal).reduce(0.0f, Float::sum);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Completed orders found",
						new OrderFilterResult(responseList, totalOfAllOrders, responseList.size())));

	}

	public ResponseEntity<ResponseObject> cancelOrder(int id, String status, String note) {
	    Optional<Order> orderOpt = repository.findById(id);
	    if (orderOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
	                HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order does not exist", ""));
	    }

	    Order order = orderOpt.get();

	    Optional<Status> newStatOpt = statusRepository.findByValue(status);
	    if (newStatOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
	                HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status does not exist", ""));
	    }

	    Status newStat = newStatOpt.get();

	    if (!order.getStatus().getValue().equals("pending") && !order.getStatus().getValue().equals("accepted")
	            && newStat.getValue().equalsIgnoreCase("cancel")) {
	        return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "You cannot cancel this order");
	    }

	    order.setStatus(newStat);
	    order.getSection().setStatus(newStat);
	    order.setDesc(note);

	    for (AccessibilityItem item : order.getSection().getItems()) {
	        item.setStatus(newStat);
	        itemRepository.save(item);
	    }

	    repository.save(order);
	    notifyRepository.save(new Notify(order, order.getAccount(), newStat.getValue()));

	    return ResponseEntity.status(HttpStatus.OK).body(
	            new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Update order successfully", order));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderFilterResult {
		private List<OrderStatisResponse> orders;
		private float totalMoneyOfAllOrders;
		private int totalOrder;
	}
}
