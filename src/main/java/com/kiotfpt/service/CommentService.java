package com.kiotfpt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Comment;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.AccountRepository;
import com.kiotfpt.repository.CommentRepository;
import com.kiotfpt.repository.OrderRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.request.CommentRequest;
import com.kiotfpt.utils.ResponseObjectHelper;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	public ResponseEntity<ResponseObject> createComment(CommentRequest commentRequest) {
		// Find the account and product
		Optional<Account> optionalAccount = accountRepository.findById(commentRequest.getAccount_id());
		Optional<Product> optionalProduct = productRepository.findById(commentRequest.getProduct_id());

		// Check if the account and product exist
		if (optionalAccount.isEmpty() || optionalProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
					String.valueOf(HttpStatus.NOT_FOUND.value()), "Account or Product not found", null));
		}

		if (commentRequest.getRate() < 0) {
			return ResponseObjectHelper.createFalseResponse(HttpStatus.BAD_REQUEST, "Rate must greater tor equal 0");
		}

		Account account = optionalAccount.get();
		Product product = optionalProduct.get();

		// Check if the account has already bought the product with status "completed"
		boolean hasBoughtProduct = orderRepository.existsByAccountAndProductAndStatus(account, product, "completed");

		if (!hasBoughtProduct) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject(false, String.valueOf(HttpStatus.BAD_REQUEST.value()),
							"Account has not bought the product with status 'completed'", null));
		}

		float averRate = 0;
		for (Comment com : product.getComments()) {
			averRate += com.getRate();
		}
		product.setRate(Math.round(averRate / product.getComments().size() * 10) / 10.0f);
		productRepository.save(product);

		// Create and save the comment
		Comment comment = new Comment(commentRequest, account, product);
		Comment savedComment = repository.save(comment);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(true,
				String.valueOf(HttpStatus.CREATED.value()), "Comment created successfully", savedComment));
	}

	public ResponseEntity<ResponseObject> getAllCommentsByAccountId(int accountId) {
		Optional<Account> optionalAccount = accountRepository.findById(accountId);

		if (!optionalAccount.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, String.valueOf(HttpStatus.NOT_FOUND.value()), "Account not found", null));
		}

		List<Comment> comments = repository.findAllByAccount(optionalAccount.get());

		List<Comment> transformedComments = comments.stream().map(comment -> new Comment(comment))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true, String.valueOf(HttpStatus.OK.value()),
				"Comments retrieved successfully", transformedComments));
	}

}
