package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kiotfpt.model.Cart;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Status;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	List<Section> findByShop(Shop shop);
	
	List<Section> findByCart(Cart cart);
	
    Optional<Section> findByShopIdAndCartIdAndStatus(int shopId, int cartId, Status status);

    @Query("SELECT s FROM Section s WHERE s.cart.id = :cartId AND s.status.id = :statusId")
    List<Section> findByCartIdAndStatusId(@Param("cartId") int cartId, @Param("statusId") int statusId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Section s WHERE s.cart.id = :cartId AND s.status.id = :statusId")
    void deleteByCartIdAndStatusId(@Param("cartId") int cartId, @Param("statusId") int statusId);

}
