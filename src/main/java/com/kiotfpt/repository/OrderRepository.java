package com.kiotfpt.repository;

import java.time.LocalDateTime;
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
	List<Order> findAllByAccount(Account acc);
	
    List<Order> findByAccountId(int accountId);

	Page<Order> findAllByShop(Shop shop, Pageable pageable);

	Optional<Order> findBySection(Section order);

	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " + "FROM Order o " + "JOIN o.section s "
			+ "JOIN s.items i " + "WHERE o.account = :account " + "AND i.variant.product = :product "
			+ "AND o.status.value = :status")
	boolean existsByAccountAndProductAndStatus(@Param("account") Account account, @Param("product") Product product,
			@Param("status") String status);

	List<Order> findByTimeCompleteBetweenAndShopIdAndStatusValue(LocalDateTime startDate, LocalDateTime endDate, int shopId,
			String status);

	List<Order> findByTimeCompleteBetweenAndStatusValue(LocalDateTime startDate, LocalDateTime endDate, String status);

    @Query("SELECT o FROM Order o WHERE o.timeInit BETWEEN ?1 AND ?2 AND (o.status.value = 'completed' OR o.status.value = 'pending')")
    List<Order> findByTimeInitBetweenAndStatusIn(LocalDateTime startDate, LocalDateTime endDate);
    
//    List<Order> findByTimeInitBetweenAndShopIdAndStatusIn(Date startDate, Date endDate, int shopId, List<String> statusList);
    @Query(value = "SELECT * FROM kiotfpt_order WHERE kiotfpt_order.order_time_init BETWEEN :startDate AND :endDate AND kiotfpt_order.shop_id = :shopId AND kiotfpt_order.status_id IN (27, 21);", nativeQuery = true)
    List<Order> findByTimeInitBetweenAndShopIdAndStatusIn(LocalDateTime startDate, LocalDateTime endDate, int shopId);
    
	@Query("SELECT o FROM Order o WHERE o.status.id = :statusId")
	List<Order> findByStatusId(int statusId);

	List<Order> findByAccountIdAndStatusId(int accountId, int statusId);

	List<Order> findByShopIdAndStatusValue(int shopId, String value);
}
