package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Address;
import com.kiotfpt.model.District;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.Province;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.AddressRepository;
import com.kiotfpt.repository.DistrictRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.ProvinceRepository;
import com.kiotfpt.repository.RoleRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.ShopRequest;
import com.kiotfpt.response.ShopResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;
import com.kiotfpt.utils.ValidationHelper;

@Service
public class ShopService {
	@Autowired
	private ShopRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private AccountProfileRepository profileRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private JavaMailSender mailSender;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getShopProfileByID() {
		Optional<Shop> shop = repository.findByAccount(tokenUtils.getAccount());
		if (!shop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], responseMessage.get("shopFound"), shop.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), ""));
	}

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

		Address address = new Address("133, Nguyen Van Linh", true, districtRepository.findById(659).get(),
				provinceRepository.findById(123).get(), profileRepository.findByAccount(account.get()).get(), null);

		account.get().setRole(roleRepository.findById(3).get());
		account.get().setStatus(statusRepository.findByValue("wait for active").get());

		accountRepository.save(account.get());

		Shop shop = new Shop(shopRequest, account.get(), address);
		repository.save(shop);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Create shop successfully. Here is your account", shop));
	}

	public ResponseEntity<ResponseObject> updateShop(int id, ShopRequest shop) {
		Optional<Shop> foundshop = repository.findById(id);
		if (foundshop.isPresent()) {
			if (shop.getName().isEmpty() || shop.getName().isBlank() || shop.getName().length() > 255)
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Name is not valid");

			ValidationHelper help = new ValidationHelper();
			if (help.isValidEmail(shop.getEmail()) == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Mail is not valid", ""));
			}

			if (help.isVietnamPhoneNumber(shop.getPhone()) == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Phone is not valid", ""));
			}

			Optional<Address> address = addressRepository.findById(shop.getAddress().getAddress_id());
			if (address.isEmpty())
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Address is not exist");

			Optional<Province> province = provinceRepository.findById(shop.getAddress().getProvince_id());
			if (province.isEmpty())
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Province is not exist");

			Optional<District> district = districtRepository.findById(shop.getAddress().getDistrict_id());
			if (district.isEmpty())
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "District is not exist");

			if (shop.getAddress().getAddress_value().length() == 0)
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input cannot be empty!");
			if (shop.getAddress().getAddress_value().length() == 255)
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input is too long!");

			Address updateAddress = address.get();
			updateAddress.setDistrict(district.get());
			updateAddress.setProvince(province.get());
			updateAddress.setValue(shop.getAddress().getAddress_value());

			foundshop.get().setEmail(shop.getEmail());
			foundshop.get().setName(shop.getName());
			foundshop.get().setPhone(shop.getPhone());
			foundshop.get().setThumbnail(shop.getThumbnail());
			foundshop.get().setAddress(updateAddress);

			repository.save(foundshop.get());

			return ResponseObjectHelper.createTrueResponse(HttpStatus.BAD_REQUEST,
					responseMessage.get("updateShopSuccess"), foundshop.get());
		}
		return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, responseMessage.get("shopNotFound"));
	}

	public ResponseEntity<ResponseObject> banShop(int shopId, String status, String note) throws MessagingException {
		Optional<Shop> optionalShop = repository.findById(shopId);
		if (!optionalShop.isPresent()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					"Shop with id: " + shopId + " not found");
		}

		Shop shop = optionalShop.get();
		Account account = shop.getAccount();
		if (account == null) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
					"No account associated with shop id: " + shopId);
		}

		// Fetch the corresponding status from the database
		Optional<Status> statusOptional = statusRepository.findByValue(status);
		if (!statusOptional.isPresent()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					status + " status not found in database");
		}

		// Update the account status
		account.setStatus(statusOptional.get());
		accountRepository.save(account);

		// Handle status with a switch case
		switch (status.toLowerCase()) {
		case "inactive":
			sendEmail(shop.getEmail(), shop.getName(), note);
			break;
		case "active":
			break;
		default:
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Invalid status provided");
		}

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Change status successfully", null);
	}

	private void sendEmail(String to, String shopName, String note) throws MessagingException {
	        String subject = "Shop Ban Notification";
	        String text = String.format(
	            "Hello %s,\n\nWe are sorry to announce that your shop has been banned with the reason: %s.\n\n" +
	            "Please contact admin through phone number 0911558539 to activate the shop again.\n\n" +
	            "Thank you.",
	            shopName, note
	        );

	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(text);

	        mailSender.send(message);
	    }

	public ResponseEntity<ResponseObject> getTop10ShopsByTransactions() {
		List<Shop> topShops = repository.findTop10ByTransactions();
		List<ShopResponse> shopResponses = topShops.stream()
				.map(shop -> new ShopResponse(shop, productRepository.findAllByShop(shop)))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
						"Top 10 shops by transactions retrieved successfully", shopResponses));
	}

	public ResponseEntity<ResponseObject> getShopByProductId(int productId) {
		// Retrieve the product by ID
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Product not found", null));
		}

		// Get the shop associated with the product
		Product product = optionalProduct.get();
		Shop shop = product.getShop();

		if (shop == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Shop not found for the product", null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Shop found", shop));

	}

}
