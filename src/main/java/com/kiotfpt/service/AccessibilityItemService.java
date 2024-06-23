package com.kiotfpt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.kiotfpt.utils.ResponseObjectHelper;

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
				.filter(s -> !s.getStatus().equals(completedStatus)).orElse(null);

		// Validate amount
		if (itemRequest.getAmount() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
					HttpStatus.BAD_REQUEST.toString().split(" ")[0], "Amount must greater than 0", null));
		}

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

	public ResponseEntity<ResponseObject> deleteItem(int id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "AccessibilityItem not found with id " + id, null));
		}
		repository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "AccessibilityItem deleted successfully", null));
	}

	@Transactional
	public ResponseEntity<ResponseObject> updateItemAmount(int itemId, int newAmount) {
		try {
			if (newAmount < 0) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
						"Quantity must be non-negative");
			}

			AccessibilityItem item = repository.findById(itemId)
					.orElseThrow(() -> new IllegalArgumentException("Item not found"));

			Variant variant = item.getVariant();
			if (variant == null) {
				throw new IllegalStateException("Item does not have an associated variant");
			}

			if (newAmount > variant.getQuantity()) {
				return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST,
						"Requested quantity exceeds available stock");
			}

			item.setQuantity(newAmount);
			item.setTotal(newAmount * variant.getPrice());

			// Update section total
			updateSectionTotal(item.getSection());

			repository.save(item);

			return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Item updated successfully", item);
		} catch (IllegalArgumentException e) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Item updated successfully");
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, "INTERNAL_SERVER_ERROR", e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject(false, "INTERNAL_SERVER_ERROR", "Failed to update item", null));
		}
	}

	private void updateSectionTotal(Section section) {
		if (section == null) {
			throw new IllegalArgumentException("Item does not have an associated section");
		}

		float newTotal = (float) section.getItems().stream()
				.mapToDouble(item -> item.getQuantity() * item.getVariant().getPrice()).sum();

		section.setTotal(newTotal);
		sectionRepository.save(section); // Save the updated section total
	}
}
