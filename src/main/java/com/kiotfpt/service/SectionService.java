package com.kiotfpt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Order;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.SectionRepository;
import com.kiotfpt.repository.ShopRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.utils.JsonReader;

@Service
public class SectionService {
	@Autowired
	private SectionRepository repository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

//	public ResponseEntity<ResponseObject> getAllOrder() {
//		List<Order> orders = repository.findAll();
//		return !orders.isEmpty()
//				? ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//								responseMessage.get("OrderFound"), orders))
//				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("OrderNotFound"), orders));
//	}
//
//	public ResponseEntity<ResponseObject> createOrder(Map<String, String> map) {
//		Optional<Shop> shop = shopRepository.findById(Integer.parseInt(map.get("shop_id")));
//		Order Order = new Order();
//		Order.setShop(shop.get());
//
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.body(new ResponseObject(true, HttpStatus.CREATED.toString().split(" ")[0],
//						responseMessage.get("createProductSuccess"), repository.save(Order)));
//	}

	public ResponseEntity<ResponseObject> updateStatusOrder(int order_id, Map<String, String> map) {
		Optional<Section> section = repository.findById(order_id);
		if (!section.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Order not found", new int[0]));
		}
		Optional<Status> status = statusRepository.findByValue(map.get("value"));
		if (!status.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], "Status not found", new int[0]));
		}
		if (status.get().getValue().equals("Delivered") || status.get().getValue().equals("Cancelled")) {
			Optional<Order> order = orderRepository.findBySection(section.get());
			Date date = new Date();
			order.get().setTimeComplete(date);
			orderRepository.save(order.get());
		}
		
		section.get().setStatus(status.get());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Order status is updated successfully",
						repository.save(section.get())));
	}

	public ResponseEntity<ResponseObject> getByShopID(int shop_id) {
		Optional<Shop> shop = shopRepository.findById(shop_id);
		if (!shop.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("shopNotFound"), new int[0]));
		}

		List<Section> orders = repository.findByShop(shop.get());
		return !orders.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								responseMessage.get("OrderFound"), orders))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("OrderNotFound"), orders));
	}

	public ResponseEntity<ResponseObject> updateSection(Section newOrder) {
		Section updateOrder = repository.findById(newOrder.getId()).map(Section -> {
			Section.setTotal((float) newOrder.getTotal());
			return repository.save(Section);
		}).orElseGet(() -> {
			return null;
		});
		if (updateOrder == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("OrderNotFound"), ""));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], responseMessage.get("updateOrderSuccess"), updateOrder));
	}

}
