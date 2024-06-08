package com.kiotfpt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.Cart;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Variant;
import com.kiotfpt.repository.AccessibilityItemRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.CartRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.repository.VariantRepository;
import com.kiotfpt.request.ItemRequest;

@Service
public class AccessibilityItemService {
	@Autowired
	private AccessibilityItemRepository repository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private VariantRepository variantRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private CartRepository cartRepository;

	public ResponseEntity<ResponseObject> createItem(ItemRequest itemRequest) {
		// Validate Account
		Optional<Account> optionalAccount = accountRepository.findById(itemRequest.getAccount_id());
		if (optionalAccount.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Account not found", null));
		}

		// Validate Cart
		Optional<Cart> optionalCart = cartRepository.findCartByAccountID(itemRequest.getAccount_id());
		if (optionalCart.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Cart not found", null));
		}
		Cart cart = optionalCart.get();

		// Validate Variant
		Optional<Variant> optionalVariant = variantRepository.findById(itemRequest.getVariant_id());
		if (optionalVariant.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Variant not found", null));
		}
		Variant variant = optionalVariant.get();

		// Fetch "in cart" status
		Status inCartStatus = statusRepository.findByValue("in cart")
				.orElseThrow(() -> new IllegalStateException("Status 'in cart' not found"));

	    // Fetch "completed" status
	    Status completedStatus = statusRepository.findByValue("completed")
	            .orElseThrow(() -> new IllegalStateException("Status 'completed' not found"));

		// Find existing section for the shop
	    Section section = sectionRepository.findByShopIdAndCartId(variant.getProduct().getShop().getId(), cart.getId())
	            .filter(s -> !s.getStatus().equals(completedStatus))
	            .orElse(null);

		// If section does not exist, create a new section
		if (section == null) {
			section = new Section();
			section.setShop(variant.getProduct().getShop());
			section.setCart(cart);
			section.setStatus(inCartStatus); // Set status to "in cart"
			section = sectionRepository.save(section);
		}

		// Check if the variant already exists in the account's cart (section)
		AccessibilityItem existingItem = repository.findByVariantIdAndSectionId(variant.getId(), section.getId())
				.orElse(null);

		if (existingItem != null) {
			// If variant exists, update the quantity
			existingItem.setQuantity(existingItem.getQuantity() + itemRequest.getAmount());
			existingItem.setTotal(existingItem.getQuantity() * variant.getPrice());
			repository.save(existingItem);
		} else {
			// Create a new item
			AccessibilityItem item = new AccessibilityItem();
			item.setQuantity(itemRequest.getAmount());
			item.setTotal(itemRequest.getAmount() * variant.getPrice());
			item.setVariant(variant);
			item.setSection(section);
			item.setNote(itemRequest.getNote() == null || itemRequest.getNote().isEmpty() ? "no note"
					: itemRequest.getNote());
			item.setStatus(inCartStatus); // Set status to "in cart"

			repository.save(item);
		}

		// Update the total in the section
		float totalSectionPrice = (float) repository.findBySection(section).stream()
				.mapToDouble(AccessibilityItem::getTotal).sum();
		section.setTotal(totalSectionPrice);
		sectionRepository.save(section);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Item added to cart successfully", null));
	}

}
