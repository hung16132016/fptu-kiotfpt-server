package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Query("SELECT c FROM Cart c WHERE c.account.account_id = :account_id")
	Optional<Cart> findCartByAccountID(@Param("account_id") int account_id);
}
