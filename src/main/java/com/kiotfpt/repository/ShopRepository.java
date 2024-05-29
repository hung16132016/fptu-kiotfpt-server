package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {

	Optional<Shop> findByAccount(Account acc);

	List<Shop> findAllByAccount(Account acc);

	@Query("SELECT s FROM Shop s ORDER BY SIZE(s.transactions) DESC")
	List<Shop> findTop10ByTransactions();
}
