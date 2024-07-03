package com.kiotfpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Follow;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Shop;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.FollowRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

@Service
public class FollowService {
	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private TokenUtils tokenUtils;

	public ResponseEntity<ResponseObject> followShop(int shopId, String status) {
		// Check if shopId and accountId exist
		Shop shop = shopRepository.findById(shopId).orElse(null);
		Account account = tokenUtils.getAccount();

		if (shop == null || account == null)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Shop or Account not found");

		Follow existingFollow = followRepository.findByShopIdAndAccountId(shopId, account.getId());

		if ("follow".equalsIgnoreCase(status)) {
			if (existingFollow != null)
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Already following this shop");

			// Create new Follow entity
			Follow follow = new Follow();
			follow.setShop(shop);
			follow.setAccount(account);

			followRepository.save(follow);

			shop.setFollower(shop.getFollower() + 1);
			shopRepository.save(shop);

			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Followed shop successfully", follow);
		} else if ("unfollow".equalsIgnoreCase(status)) {
			if (existingFollow == null)
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Not following this shop");

			followRepository.delete(existingFollow);

			shop.setFollower(shop.getFollower() - 1);
			shopRepository.save(shop);

			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Unfollowed shop successfully", null);
		} else {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
					"Invalid status. Use 'follow' or 'unfollow'.");
		}
	}

	public ResponseEntity<ResponseObject> checkfollowShop(int shopId, int accountId) {
		// Check if shopId and accountId exist
		Shop shop = shopRepository.findById(shopId).orElse(null);
		Account account = accountRepository.findById(accountId).orElse(null);

		if (shop == null || account == null)
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Shop or Account not found");

		// Check if the follow relationship already exists
		Follow existingFollow = followRepository.findByShopIdAndAccountId(shopId, accountId);
		if (existingFollow != null)
			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "This account has already followed this shop",
					null);

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "This account does not follow this shop", null);
	}

}
