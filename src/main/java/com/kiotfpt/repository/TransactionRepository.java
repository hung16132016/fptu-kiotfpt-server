package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
//	@Query(value = "SELECT * FROM shop WHERE MONTH(date) = :month", nativeQuery = true)
	List<Transaction> findAllByAccount(Account acc);
//	List<Transaction> findByMonth (int month);
	List<Transaction> findAllByShop(Shop shop);
	Optional<Transaction> findBySection(Section order);
}
