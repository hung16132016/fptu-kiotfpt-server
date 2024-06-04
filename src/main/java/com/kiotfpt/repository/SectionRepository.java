package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Cart;
import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	List<Section> findByShop(Shop shop);
	
	List<Section> findByCart(Cart cart);
	
    Optional<Section> findByShopIdAndCartId(int shopId, int accountId);

}
