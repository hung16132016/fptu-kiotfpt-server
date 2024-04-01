package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.CompositeKey_LineItem;
import com.kiotfpt.model.Accessibility_item;

@Repository
public interface SubOrderRepository extends JpaRepository<Accessibility_item, CompositeKey_LineItem>{

	@Query(value = "SELECT * FROM line_item l WHERE l.transaction_id = ?1", nativeQuery = true)
	List<Accessibility_item> findAllByTransactionId(int transaction_id);
	
	@Query(value = "SELECT * FROM line_item l WHERE l.order_id = ?1", nativeQuery = true)
	List<Accessibility_item> findAllByOrderId(int order_id);
}
