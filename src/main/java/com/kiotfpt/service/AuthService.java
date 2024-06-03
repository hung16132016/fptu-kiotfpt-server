package com.kiotfpt.service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Cart;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Role;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.CartRepository;
import com.kiotfpt.repository.RoleRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.AccountSignUpRequest;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.MD5;
import com.kiotfpt.utils.ValidationHelper;

@Service
public class AuthService {
	@Autowired
	private AccountRepository repository;

	@Autowired
	private ShopRepository shoprepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private AccountProfileRepository profileRepository;

	private ValidationHelper validationHelper = new ValidationHelper();

	@Autowired
	private JavaMailSender mailSender;
//	@Autowired
//	private AccountProfileRepository profileRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	// fix
	public ResponseEntity<ResponseObject> signIn(String username, String password) {
		if (username.trim() == "" || password.trim() == "")
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input can not be empty", new int[0]));

		Optional<Account> account = repository.findByUsername(username);
		if (!account.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("accountNotFound"), new int[0]));
		} else if (!account.get().getPassword().equals(MD5.generateMD5Hash(password)))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Wrong password", new int[0]));

		if (account.get().getStatus().getValue().equals("inactive"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							responseMessage.get("accountNotActivate"), new int[0]));

		Map<String, String> map = new HashMap<>();
		map.put("account_id", String.valueOf(account.get().getId()));
		map.put("role", account.get().getRole().getValue());
		if (map.get("role").equals("Seller")) {
			Optional<Shop> shop = shoprepository.findByAccount(account.get());
			map.put("shop_id", String.valueOf(shop.get().getId()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("signInSuccess"), map));
	}

	// fix
	public ResponseEntity<ResponseObject> signUp(AccountSignUpRequest request)
			throws AddressException, MessagingException {
		Map<String, String> errors = new HashMap<>();
		Optional<Account> foundAccount = repository.findByUsername(request.getUsername());

		if (!foundAccount.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject(false,
					HttpStatus.CONFLICT.toString().split(" ")[0], responseMessage.get("userNameExisted"), ""));
		} else {

			if (request.getUsername().strip().equals("")) {
				errors.put("emptyUserName", "Username can't be empty!");
			} else if (request.getUsername().strip().length() < 6 || request.getUsername().strip().length() > 100) {
				errors.put("usernameLength", "Username's length must be from 6 to 100!");
			} else if (!validationHelper.isValidEmail(request.getUsername().strip())) {
				errors.put("usernameNotEmail", "Username must follow format email!");
			}

			if (request.getPassword().strip().equals("")) {
				errors.put("emptyPassword", "Password can't be empty!");
			} else if (request.getPassword().strip().length() < 6 || request.getPassword().strip().length() > 32) {
				errors.put("passwordLength", "Password's length must be from 6 to 32!");
			}

			if (!request.getRetypePassword().strip().equals(request.getPassword().strip())) {
				errors.put("retypePasswordNotCorrect", "Password and retype password not correct!");
			}

			if (errors.isEmpty()) {
				Optional<Role> role = roleRepository.findById(2);
				Optional<Status> status = statusRepository.findById(15);

				Account accountSignUp = new Account();
				accountSignUp.setRole(role.get());
				accountSignUp.setUsername(request.getUsername().strip());
				accountSignUp.setPassword(MD5.generateMD5Hash(request.getPassword().strip()));
				accountSignUp.setStatus(status.get());

				accountSignUp = repository.save(accountSignUp);

				Cart newCart = new Cart();
				newCart.setAccount(accountSignUp);
				cartRepository.save(newCart);

				AccountProfile newProfile = new AccountProfile();
				newProfile.setAccount(accountSignUp);
				profileRepository.save(newProfile);

				MimeMessage message = mailSender.createMimeMessage();
				message.setFrom(new InternetAddress("kiotfpt.help@gmail.com"));
				message.setRecipients(MimeMessage.RecipientType.TO, request.getUsername().strip());
				message.setSubject("Email for sign up account");
				String htmlContent = "<h2>Welcome to Kiot FPT!</h2>"
						+ "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>" + "<p>Hello, "
						+ request.getUsername().strip() + "</p>"
						+ "<p>Thank you for submitting an account registration request on Mappe!</p>"
						+ "<p>Please do not share the link with anyone else to avoid losing your account.</p>"
						+ "<p>Please click this link " + "<a href=\"http://localhost:8888/v1/auth/confirm-sign-up/"
						+ request.getUsername().strip() + "\">"
						+ "<p style='font-size: 24px;'><strong>confirm sign-up</strong></p>"
						+ "</a> to activate your account.</p>" + "</div>";
				message.setContent(htmlContent, "text/html; charset=utf-8");
				mailSender.send(message);

				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], responseMessage.get("signUpSuccess"), accountSignUp));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
							responseMessage.get("signUpFailed"), errors.values()));
		}
	}

	public ResponseEntity<ResponseObject> activeAccount(String username) {
		Optional<Account> foundAccount = repository.findByUsername(username);
		if (!foundAccount.isEmpty()) {
			Account activeAccount = foundAccount.get();
			if (activeAccount.getStatus().getId() == 15) {
				Optional<Status> status = statusRepository.findById(11);
				activeAccount.setStatus(status.get());
				repository.save(activeAccount);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], "Active account successfull", ""));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "This account can not be active", ""));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
				HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Could not find any account with this username", ""));
	}

	public ResponseEntity<ResponseObject> forgotPassword(String username) throws AddressException, MessagingException {
		Optional<Account> foundAccount = repository.findByUsername(username);
		if (!foundAccount.isEmpty()) {
			Account account = foundAccount.get();
			String newPassword = generateRandomPassword();
			String newPasswordMD5 = MD5.generateMD5Hash(newPassword);
			account.setPassword(newPasswordMD5);
			repository.save(account);

			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(new InternetAddress("kiotfpt.help@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, username);
			message.setSubject("Reset Kiot FPT Password");
			String htmlContent = "<h2>Welcome to Kiot FPT!</h2>"
					+ "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>" + "<p>Hello, "
					+ username + "</p>" + "<p>This is your new password:</p>"
					+ "<p>Please do not share the password with anyone else to avoid losing your account.</p>"
					+ "<p>Please click this " + newPassword + "</p>" + "</div>";
			message.setContent(htmlContent, "text/html; charset=utf-8");
			mailSender.send(message);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Send new password to email seccessfull", ""));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
				HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Could not find any account with this username", ""));
	}

	public ResponseEntity<ResponseObject> sendmail() throws AddressException, MessagingException, JsonProcessingException {

		Optional<Account> acc = repository.findById(1);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    String json = objectMapper.writeValueAsString(acc.get());
        System.out.println(json);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("signUpSuccess"), json.replaceAll("\\\\", "")));
	}

	private String generateRandomPassword() {
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(8);
		SecureRandom random = new SecureRandom();
		while (sb.length() < 8) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(randomIndex));
		}
		return sb.toString();
	}

}