package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	@Query(value = "SELECT * FROM cart c WHERE c.account_id = ?1", nativeQuery = true)
	List<Cart> findAllByAccountId(int id);
}
