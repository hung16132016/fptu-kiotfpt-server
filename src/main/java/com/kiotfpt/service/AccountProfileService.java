package com.kiotfpt.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.request.UpdatePasswordRequest;
import com.kiotfpt.response.ProfileStatisResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.MD5;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;
import com.kiotfpt.utils.ValidationHelper;

@Service
public class AccountProfileService {

	@Autowired
	private AccountProfileRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();
	private ValidationHelper validator = new ValidationHelper();

	public ResponseEntity<ResponseObject> getProfileByAccountID() {
		Optional<Account> account = accountRepository.findById(tokenUtils.getAccount().getId());
		if (account.isPresent()) {
			if (account.get().getStatus().getValue().equals("inactive"))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
								responseMessage.get("accountNotActivate"), new int[0]));

			Optional<AccountProfile> profile = repository.findByAccount(account.get());
			if (profile.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
						HttpStatus.OK.toString().split(" ")[0], responseMessage.get("profileFound"), profile.get()));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("profileNotFound"), ""));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("profileNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> updateProfile(AccountProfile request) {
		Optional<AccountProfile> accountProfile = repository.findById(request.getId());
		Optional<AccountProfile> accountToken = repository.findByAccount(tokenUtils.getAccount());

		if (accountProfile.get().getId() != accountToken.get().getId()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
		}

		if (accountProfile.isEmpty()) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					responseMessage.get("profileNotFound"));
		}

		AccountProfile updateProfile = accountProfile.get();
		if (request.getEmail().strip().equals("") || request.getPhone().strip().equals("")
				|| request.getName().strip().equals(""))
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input can not be empty!");

		if (!validator.isValidEmail(request.getEmail()))
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Email is incorrect format!");

		if (!validator.isVietnamPhoneNumber(request.getPhone()))
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Phone is incorrect format!");

		if (request.getEmail().length() > 255 || request.getThumbnail().length() > 255
				|| request.getName().length() > 255)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Input is too long!");

		updateProfile.setEmail(request.getEmail());
		updateProfile.setName(request.getName());
		updateProfile.setPhone(request.getPhone());
		updateProfile.setThumbnail(request.getThumbnail());
		if (request.getBirthday() != null)
			updateProfile.setBirthday(request.getBirthday());

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Update profile seccessfull",
				repository.save(updateProfile));

	}

	public ResponseEntity<ResponseObject> updatePassword(UpdatePasswordRequest request) {
		Optional<Account> accountFound = accountRepository.findById(request.getAccount_id());
		if (accountFound.isPresent()) {
			if (request.getOldPassword().strip().equals("") || request.getNewPassword().strip().equals("")
					|| request.getRetypePassword().strip().equals(""))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Input can not be empty!", new int[0]));
			if (request.getNewPassword().length() > 32)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Your new password is too long!", new int[0]));
			if (!request.getNewPassword().equals(request.getRetypePassword()))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
								"New password and retype password is not the same!", new int[0]));
			String newPassword = MD5.generateMD5Hash(request.getOldPassword());
			if (!newPassword.equals(accountFound.get().getPassword()))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
						HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Old password is wrong!", new int[0]));
			Account account = accountFound.get();
			account.setPassword(newPassword);
			accountRepository.save(account);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Update password seccessfull", new int[0]));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
				HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("profileNotFound"), ""));
	}

	public ResponseEntity<ResponseObject> getProfilesOrderedByTotalSpent(int shopId) {
		try {
			List<Order> orders = orderRepository.findByShopIdAndStatusValue(shopId, "completed");
			List<AccountProfile> profiles = repository.findAll();

			Map<Integer, Double> totalSpentByAccount = orders.stream().collect(Collectors
					.groupingBy(order -> order.getAccount().getId(), Collectors.summingDouble(Order::getTotal)));

			List<ProfileStatisResponse> profileResponses = profiles.stream().map(profile -> {
				double totalSpent = totalSpentByAccount.getOrDefault(profile.getAccount().getId(), 0.0);
				ProfileStatisResponse response = new ProfileStatisResponse(profile);
				response.setTotalSpent(totalSpent);
				return response;
			}).sorted(Comparator.comparingDouble(ProfileStatisResponse::getTotalSpent).reversed())
					.collect(Collectors.toList());

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
							"Profiles retrieved and sorted successfully", profileResponses));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, HttpStatus.INTERNAL_SERVER_ERROR.toString().split(" ")[0],
							"An error occurred while retrieving profiles", null));
		}
	}
}
