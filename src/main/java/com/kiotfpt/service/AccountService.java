package com.kiotfpt.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountProfileRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.response.ProfileStatisResponse;
import com.kiotfpt.utils.JsonReader;

@Service
public class AccountService {

	@Autowired
	private AccountProfileRepository profilerepository;

	@Autowired
	private OrderRepository orderRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAllAccount() {
		try {
			List<Order> orders = orderRepository.findByStatusId(27);
			List<AccountProfile> profiles = profilerepository.findAll();

			Map<Integer, Double> totalSpentByAccount = orders.stream().collect(Collectors
					.groupingBy(order -> order.getAccount().getId(), Collectors.summingDouble(Order::getTotal)));

			List<ProfileStatisResponse> profileResponses = profiles.stream()
					.filter(profile -> profile.getAccount().getRole().getId() == 2).map(profile -> {
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
