package com.kiotfpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Notify;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.NotifyRepository;
import com.kiotfpt.utils.JsonReader;
import com.kiotfpt.utils.ResponseObjectHelper;
import com.kiotfpt.utils.TokenUtils;

@Service
public class NotifyService {

	@Autowired
	private NotifyRepository repository;

	@Autowired
	private TokenUtils tokenUtils;

	HashMap<String, String> responseMessage = new JsonReader().readJsonFile();

	public ResponseEntity<ResponseObject> getNotifyByAccountID() {
		Account acc = tokenUtils.getAccount();
		List<Notify> notifies = repository.findAllByAccount(acc);
		if (notifies.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND, "Notifies do not exist");

		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Notifies found", notifies);

	}

	public ResponseEntity<ResponseObject> deleteNotifyById(int notify_id) {
		Optional<Notify> notify = repository.findById(notify_id);

		if (notify.get().getAccount().getId() != tokenUtils.getAccount().getId())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");

		if (notify.isEmpty())
			return ResponseObjectHelper.createFalseResponse(HttpStatus.NOT_FOUND,
					responseMessage.get("notifyNotFound"));

		repository.deleteById(notify_id);
		return ResponseObjectHelper.createTrueResponse(HttpStatus.OK, "Notify deleted successfully", new int[0]);

	}

}
