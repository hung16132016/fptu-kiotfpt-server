//package com.kiotfpt.controller;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.kiotfpt.model.ResponseObject;
//import com.kiotfpt.model.SubOrder;
//import com.kiotfpt.service.SubOrderService;
//
//@CrossOrigin(origins = "http://localhost:8080")
//@RestController
//@RequestMapping("/api/v1/sub-order")
//@Service
//public class SubOrderController {
//
//	@Autowired
//	private SubOrderService service;
//
////	@GetMapping("/line-item")
////	public ResponseEntity<ResponseObject> getAllSubOrder() {
////		return service.getAllSubOrder();
////	}
////
////	@GetMapping("/line-item/{id}")
////	public ResponseEntity<ResponseObject> getSubOrderId(@PathVariable int id) {
////		return service.getBySubOrderId(id);
////	}
//
//	@DeleteMapping("/delete")
//	public ResponseEntity<ResponseObject> deleteSubOrder(@RequestBody SubOrder subOrder, HttpServletRequest request) {
//		return service.deleteSubOrder(subOrder,request);
//	}
//	
//	@PutMapping("/update")
//	public ResponseEntity<ResponseObject> updateSubOrder(@RequestBody SubOrder newSubOrder, HttpServletRequest request) {
//		return service.updateSubOrder(newSubOrder,request);
//	}
//
//	// create new line item
//	@PostMapping("/create")
//	public ResponseEntity<ResponseObject> createSubOrder(@RequestBody Map<String, Integer> newSubOrder,HttpServletRequest request) {
//		return service.createSubOrder(newSubOrder,request);
//	}
//
//}
