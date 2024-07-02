package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Account;
import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Cart;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ProductCondition;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.model.Variant;
import com.kiotfpt.repository.AccessibilityItemRepository;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.CartRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.response.Accessibility_itemResponse;
import com.kiotfpt.response.SectionResponse;
import com.kiotfpt.response.ShopMiniResponse;
import com.kiotfpt.response.StatusResponse;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.TokenUtils;

@Service
public class CartService {

	@Autowired
	private CartRepository repository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccessibilityItemRepository itemRepository;

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private TokenUtils tokenUtils;

//	@Autowired
//	private AddressDeliverRepository addRepository;
	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getAmountCartByAccountID(int accountID) {
		Optional<Account> account = accountRepository.findById(accountID);
		int count = 0;
		if (account.isEmpty())
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));

		Optional<Cart> cart = repository.findCartByAccountID(accountID);
		if (cart.isEmpty())
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), ""));

		for (Section section : cart.get().getSections()) {
			if (section.getStatus().getId() != 31)
				continue;
			for (AccessibilityItem item : section.getItems()) {
				if (item.getStatus().getId() != 31)
					continue;
				count = count + item.getQuantity();
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("cartFound"), count));
	}

	public ResponseEntity<ResponseObject> getCartByID() {
		Optional<Cart> cart = repository.findCartByAccountID(tokenUtils.getAccount().getId());
		if (!cart.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), ""));
		}

		List<Section> sections = sectionRepository.findByCart(cart.get());
		if (sections.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Sections do not exist", ""));
		}

		// Filter sections with status ID 31
		sections = sections.stream().filter(section -> section.getStatus().getId() == 31).collect(Collectors.toList());

		if (sections.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Sections with status ID 31 do not exist", ""));
		}

		List<SectionResponse> list = new ArrayList<>();
		for (Section section : sections) {
			List<AccessibilityItem> list_item = itemRepository.findBySection(section);
			list_item = list_item.stream().filter(item -> item.getStatus().getId() == 31).collect(Collectors.toList());

			if (list_item.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Items do not exist", ""));
			}

			List<Accessibility_itemResponse> items = new ArrayList<>();
			for (AccessibilityItem item : list_item) {
				Variant var = item.getVariant();
				if (var == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Variant of item: " + item.getId() + " not found", ""));
				}

				Product product = var.getProduct();
				if (product == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Product of item: " + item.getId() + " not found", ""));
				}

				ProductCondition condition = product.getCondition();
				if (condition == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Condition of product id: " + product.getId() + " not found", ""));
				}

				Brand brand = product.getBrand();
				if (brand == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Brand of product id: " + product.getId() + "not found", ""));
				}

				Status status = product.getStatus();
				if (status == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Status of product id: " + product.getId() + "not found", ""));
				}

				Category category = product.getCategory();
				if (category == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
									"Category of product id: " + product.getId() + "not found", ""));
				}

				Accessibility_itemResponse item_res = new Accessibility_itemResponse(item, product);
				items.add(item_res);
			}

			Shop get_shop = section.getShop();
			ShopMiniResponse shop = new ShopMiniResponse(get_shop);
			StatusResponse status = new StatusResponse(section.getStatus());

			SectionResponse response = new SectionResponse(section.getId(), section.getTotal(), shop, status, items);
			list.add(response);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Section found", list));
	}

    public ResponseEntity<ResponseObject> deleteSectionsByStatusInCart(int cartId) {
        if (!repository.existsById(cartId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Cart not found with id " + cartId, null));
        }

        Optional<Status> status = statusRepository.findByValue("in cart");
        // First, delete all AccessibilityItem entities associated with the sections
        sectionRepository.findByCartIdAndStatusId(cartId, status.get().getId()).forEach(section -> {
            itemRepository.deleteBySectionId(section.getId());
        });

        // Then delete the sections
        sectionRepository.deleteByCartIdAndStatusId(cartId, status.get().getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Sections with status " + status.get().getValue() + " deleted successfully", null));
    }

}
