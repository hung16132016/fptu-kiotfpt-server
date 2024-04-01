//package com.kiotfpt.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.kiotfpt.model.Account;
//import com.kiotfpt.model.CompositeKey_LineItem;
//import com.kiotfpt.model.Order;
//import com.kiotfpt.model.Product;
//import com.kiotfpt.model.ResponseObject;
//import com.kiotfpt.model.SubOrder;
//import com.kiotfpt.repository.AccountRepository;
//import com.kiotfpt.repository.OrderRepository;
//import com.kiotfpt.repository.ProductRepository;
//import com.kiotfpt.repository.SubOrderRepository;
//import com.kiotfpt.utils.JsonReader;
//import com.kiotfpt.utils.TokenUtils;
//import com.mappe.model.Cart;
//import com.mappe.repository.CartRepository;
//
//@Service
//public class SubOrderService {
//
//	@Autowired
//	private SubOrderRepository repository;
//	@Autowired
//	private AccountRepository accrepository;
//	@Autowired
//	private ProductRepository productRepository;
//	@Autowired
//	private CartRepository cartRepository;
//	@Autowired
//	private OrderRepository orderRepository;
//	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();
//
////	public ResponseEntity<ResponseObject> getAllSubOrder() {
////		List<SubOrder> subOrders = repository.findAll();
////		return !subOrders.isEmpty()
////				? ResponseEntity.status(HttpStatus.OK)
////						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
////								responseMessage.get("cartFound"), subOrders))
////				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
////						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), ""));
////	}
////
////	public ResponseEntity<ResponseObject> getBySubOrderId(int sub_id) {
////		List<SubOrder> subOrders = repository.findAllByOrderId(sub_id);
////		return !subOrders.isEmpty()
////				? ResponseEntity.status(HttpStatus.OK)
////						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
////								responseMessage.get("cartFound"), subOrders))
////				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
////						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("cartNotFound"), subOrders));
////	}
//
//	public ResponseEntity<ResponseObject> handleSubOrder(Map<String, List<SubOrder>> list, HttpServletRequest request) {
//		String checkToken = new TokenUtils().checkRole(1, request, accrepository);
//		switch (checkToken) {
//		case "ok":
//			for (SubOrder i : list.get("data")) {
//				if (i.getOrder().getStatus() == 0) {
//					Order newOrder = new Order();
//					newOrder.setTotal(0);
//					newOrder.setStatus(1);
//					newOrder.setShop(i.getProduct().getShop());
//					newOrder.setCart(i.getOrder().getCart());
//					orderRepository.save(newOrder);
//					i.getId().setOrder_id(newOrder.getId());
//					i.setOrder(newOrder);
//					repository.save(i);
//				}
//			}
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject(false, HttpStatus.OK.toString().split(" ")[0], "ok", ""));
//		case "unauthorized":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		case "empty":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		default:
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//		}
//	}
//
//	public ResponseEntity<ResponseObject> deleteSubOrder(SubOrder delsubOrder, HttpServletRequest request) {
//		Optional<Product> product = productRepository.findById(delsubOrder.getId().getProductId());
//		if (product.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//							responseMessage.get("notFindProduct") + delsubOrder.getId().getProductId(), ""));
//		}
//		Optional<Order> order = orderRepository.findById(delsubOrder.getId().getOrderId());
//		if (order.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("notFindOrder"), ""));
//		}
//		String checkToken = new TokenUtils().checkMatch(order.get().getCart().getAccount().getID(), request,
//				accrepository);
//		switch (checkToken) {
//		case "ok":
//			Optional<SubOrder> subOrder = repository.findById(delsubOrder.getId());
//			if (subOrder.isPresent()) {
//				subOrder.get().getOrder().setTotal(subOrder.get().getOrder().getTotal() - subOrder.get().getTotal());
//				repository.delete(delsubOrder);
//				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(false,
//						HttpStatus.OK.toString().split(" ")[0], responseMessage.get("deleteSubOrderSuccess"), ""));
//
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("subOrderNotFound"), ""));
//			}
//		case "unauthorized":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		case "empty":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		default:
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//		}
//	}
//
//	public ResponseEntity<ResponseObject> updateSubOrder(SubOrder newSubOrder, HttpServletRequest request) {
//		Optional<Product> product = productRepository.findById(newSubOrder.getId().getProductId());
//		if (product.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//							responseMessage.get("notFindProduct") + newSubOrder.getId().getProductId(), ""));
//		}
//		Optional<Order> order = orderRepository.findById(newSubOrder.getId().getOrderId());
//		if (order.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//					HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("notFindOrder"), ""));
//		}
//		String checkToken = new TokenUtils().checkMatch(order.get().getCart().getAccount().getID(), request,
//				accrepository);
//		switch (checkToken) {
//		case "ok":
//			if (newSubOrder.getQuantity() <= 0) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(false,
//						HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("quantityWrong"), ""));
//			}
//			SubOrder updateSubOrder = repository.findById(newSubOrder.getId()).map(SubOrder -> {
//				SubOrder.setTotal(SubOrder.getTotal()
//						+ (newSubOrder.getQuantity() - SubOrder.getQuantity() * product.get().getPrice()));
//				SubOrder.setQuantity(newSubOrder.getQuantity());
//				return repository.save(SubOrder);
//			}).orElseGet(() -> {
//				return null;
//			});
//			if (updateSubOrder == null) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//						HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("subOrderNotFound"), ""));
//			} else {
//				order.get().setTotal(order.get().getTotal() + updateSubOrder.getTotal());
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
//								responseMessage.get("updateSubOrderSuccess"), updateSubOrder));
//			}
//		case "unauthorized":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		case "empty":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		default:
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//		}
//	}
//
//	public ResponseEntity<ResponseObject> createSubOrder(Map<String, Integer> newSubOrder, HttpServletRequest request) {
//		String checkToken = new TokenUtils().checkRole(1, request, accrepository);
//		switch (checkToken) {
//		case "ok":
//			Account acc = accrepository.findByToken(request.getHeader("Authorization").split(" ")[1]);
//			Map<String, String> error = new HashMap<>();
//			if (newSubOrder.get("productId") == null) {
//				error.put("productIdEmpty", "ProductId can't be empty!");
//			}
//			if (newSubOrder.get("quantity") == null) {
//				error.put("quantityEmpty", "Quantity can't be empty!");
//			} else if (newSubOrder.get("quantity") <= 0) {
//				error.put("quantityError", "Quantity must be at least 1!");
//			}
//			if (error.isEmpty()) {
//				Order newOrder = new Order();
//				Optional<Product> product = productRepository.findById(newSubOrder.get("productId"));
//				if (product.isEmpty()) {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//							.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//									responseMessage.get("notFindProduct") + newSubOrder.get("productId"), ""));
//				}
//				List<Cart> cartList = cartRepository.findAllByAccountID(acc.getID());
//				boolean checkExist = false;
//				for (Order i : cartList.get(0).getOrders()) {
//					if (i.getShop().getID() == product.get().getShop().getID()) {
//						checkExist = true;
//						newOrder = i;
//					}
//				}
//				if (checkExist == false) {
//					newOrder.setTotal(0);
//					newOrder.setStatus(0);
//					try {
//						newOrder.setShop(product.get().getShop());
//					} catch (NullPointerException e) {
//						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//								.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//										responseMessage.get("shopNotFound") + newSubOrder.get("productId"), ""));
//					}
//					newOrder.setCart(cartList.get(0));
//					orderRepository.save(newOrder);
//				}
//				SubOrder sub = new SubOrder();
//				CompositeKey_LineItem id = new CompositeKey_LineItem();
//				id.setOrder_id(newOrder.getId());
//				id.setProductId(newSubOrder.get("productId"));
//				sub.setId(id);
//
//				sub.setQuantity(newSubOrder.get("quantity"));
//				sub.setStatus(0);
//				sub.setTotal(newSubOrder.get("quantity") * product.get().getPrice());
//
//				newOrder.setTotal(newOrder.getTotal() + sub.getTotal());
//				orderRepository.save(newOrder);
//
//				sub.setOrder(newOrder);
//				sub.setProduct(product.get());
//				if (repository.findById(sub.getId()).isEmpty()) {
//					repository.save(sub);
//					return ResponseEntity.status(HttpStatus.CREATED)
//							.body(new ResponseObject(true, HttpStatus.CREATED.toString().split(" ")[0],
//									responseMessage.get("createSubOrderSuccess"), sub));
//				} else
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(true,
//							HttpStatus.BAD_REQUEST.toString().split(" ")[0], responseMessage.get("dataDuplicate"), ""));
//
//			} else
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body(new ResponseObject(false, HttpStatus.BAD_REQUEST.toString().split(" ")[0],
//								"Please check the following errors:", error.values()));
//		case "unauthorized":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		case "empty":
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(false,
//					HttpStatus.UNAUTHORIZED.toString().split(" ")[0], responseMessage.get("unauthorized"), ""));
//		default:
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
//					HttpStatus.NOT_FOUND.toString().split(" ")[0], responseMessage.get("tokenNotFound"), ""));
//		}
//	}
//}
