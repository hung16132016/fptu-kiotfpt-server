package com.kiotfpt.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Order;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
//	@Query(value = "SELECT * FROM shop WHERE MONTH(date) = :month", nativeQuery = true)
	List<Order> findAllByAccount(Account acc);

//	List<Transaction> findByMonth (int month);
	Page<Order> findAllByShop(Shop shop, Pageable pageable);

	Optional<Order> findBySection(Section order);

	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " + "FROM Order o " + "JOIN o.section s "
			+ "JOIN s.items i " + "WHERE o.account = :account " + "AND i.variant.product = :product "
			+ "AND o.status.value = :status")
	boolean existsByAccountAndProductAndStatus(@Param("account") Account account, @Param("product") Product product,
			@Param("status") String status);
	
    List<Order> findByTimeCompleteBetweenAndShopIdAndStatusValue(Date startDate, Date endDate, int shopId, String status);

    List<Order> findByTimeCompleteBetweenAndStatusValue(Date startDate, Date endDate, String status);
}
