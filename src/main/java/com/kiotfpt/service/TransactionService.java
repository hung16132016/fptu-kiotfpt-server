package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Transaction;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.TransactionRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private SectionRepository orderRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ProductRepository productRepository;
//	@Autowired
//	private JavaMailSender mailSender;

	public String randomNumber;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getTransactionByAccountID(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Transaction> transactions = repository.findAllByAccount(acc.get());
			if (!transactions.isEmpty()) {
				List<Transaction> returnListTransactions = new ArrayList<>(); // List to store products with status not 2, 3, or
																		// 4
				// Iterate through foundProduct list to check status
				for (Transaction transaction : transactions) {
					int status = transaction.getSection().getStatus().getStatus_id();
					// Check if status is not 2, 3, or 4
					if (status != 1 && status != 2 && status != 3 && status != 4) {
						returnListTransactions.add(transaction);
					}
				}
				if (!returnListTransactions.isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdSuccess"), returnListTransactions));
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
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						responseMessage.get("accountNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> getTransactionByShopID(int shop_id) {
		Optional<Shop> shop = shopRepository.findById(shop_id);
		List<Transaction> transactions = repository.findAllByShop(shop.get());
		return !transactions.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								responseMessage.get("transactionFound"), transactions))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("transactionNotFound"), ""));
	}
	
	public ResponseEntity<ResponseObject> getCurrentTransactions(int account_id) {
		Optional<Account> acc = accountRepository.findById(account_id);
		if (!acc.isEmpty()) {
			List<Transaction> transactions = repository.findAllByAccount(acc.get());
			if (!transactions.isEmpty()) {
				List<Transaction> returnListTransactions = new ArrayList<>(); // List to store products with status not 2, 3, or
																		// 4
				// Iterate through foundProduct list to check status
				for (Transaction transaction : transactions) {
					int status = transaction.getSection().getStatus().getStatus_id();
					// Check if status is not 2, 3, or 4
					if (status == 1 || status == 2 || status == 3 || status == 4) {
						returnListTransactions.add(transaction);
					}
				}
				if (!returnListTransactions.isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
									responseMessage.get("getProductByShopIdSuccess"), returnListTransactions));
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
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
						responseMessage.get("accountNotFound"), ""));
	}

//	public ResponseEntity<ResponseObject> updateTransaction(Transaction newTransaction) {
//		Transaction updateTransaction = repository.findById(newTransaction.getID()).map(transaction -> {
//			transaction.setTotal(newTransaction.getTotal());
//			transaction.setDesciption(newTransaction.getDesciption());
//			transaction.setStatus(newTransaction.getStatus());
//			transaction.setTime(newTransaction.getTime());
//			return repository.save(transaction);
//		}).orElseGet(() -> {
//			return null;
//		});
//		if (updateTransaction == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("transactionNotFound"), ""));
//		} else {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(false, HttpStatus.OK.toString().split(" ")[0],
//							responseMessage.get("updateTransactionSuccess"), updateTransaction));
//		}
//	}
//
	public ResponseEntity<ResponseObject> createTransaction(Map<String, String> map) {

		Map<String, String> errors = new HashMap<>();
		Optional<Account> account = accountRepository.findById(Integer.parseInt(map.get("account_id")));
		if (!account.isPresent()) {
			errors.put("accoundNotExist", "Account does not exist!");
		}
		Optional<Product> product = productRepository.findById(Integer.parseInt(map.get("product_id")));
		if (!product.isPresent()) {
			errors.put("productNotExist", "Product does not exist!");
		}

		if (errors.isEmpty()) {

			Status status = statusRepository.findById(8).orElse(null);
			Section section = new Section();
			section.setSection_total(product.get().getProduct_price());
			section.setShop(product.get().getShop());
			section.setStatus(status);
			orderRepository.save(section);

			Transaction transaction = new Transaction();
			Date date = new Date();
			transaction.setTransaction_time_init(date);
			transaction.setTransaction_time_complete(null);
			transaction.setAccount(account.get());
			transaction.setTransaction_desc(product.get().getProduct_description());
			transaction.setShop(product.get().getShop());
			transaction.setTransaction_total(product.get().getProduct_price());
			transaction.setSection(section);

			repository.save(transaction);
//					MimeMessage message = mailSender.createMimeMessage();
//					message.setFrom(new InternetAddress("mappe.help@gmail.com"));
//					message.setRecipients(MimeMessage.RecipientType.TO, account.get().getAccountProfile().getEmail());
//					String htmlContent = "<h1>OTP code for payment</h1>"
//							+ "<p>Please type this code to confirm your payment: </p>" + randomNumber;
//					message.setSubject("Email for your payment");
//					message.setContent(htmlContent, "text/html; charset=utf-8");
//					mailSender.send(message);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("checkMail"), ""));

		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							responseMessage.get("paymentFailed"), errors.values()));
	}
//
//	public ResponseEntity<ResponseObject> checkOtpPayment(Map<String, String> obj, HttpServletRequest request) {
//		Optional<Transaction> transaction = repository.findById(Integer.parseInt(obj.get("transaction_id")));
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
//							responseMessage.get("notFoundTransaction") + obj.get("transaction_id"), ""));
//	}
}
