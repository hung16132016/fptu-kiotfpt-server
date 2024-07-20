package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Brand;
import com.kiotfpt.model.Category;
import com.kiotfpt.model.ShopCategory;

@Repository
public interface ShopCateRepository extends JpaRepository<ShopCategory, Integer> {
	
    @Query("SELECT sc.category FROM ShopCategory sc WHERE sc.shop.id = :shopId")
    List<Category> findCategoriesByShopId(int shopId);

	Optional<ShopCategory> findByIdAndStatusValue(int id, String string);

}
