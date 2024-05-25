package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.ShopCategory;

@Repository
public interface ShopCateRepository extends JpaRepository<ShopCategory, Integer> {
	
}
