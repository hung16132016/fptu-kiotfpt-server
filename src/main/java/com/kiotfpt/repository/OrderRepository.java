package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
//	@Query(value = "SELECT * FROM shop WHERE MONTH(date) = :month", nativeQuery = true)
	List<Order> findAllByAccount(Account acc);

//	List<Transaction> findByMonth (int month);
	List<Order> findAllByShop(Shop shop);

	Optional<Order> findBySection(Section order);
}
