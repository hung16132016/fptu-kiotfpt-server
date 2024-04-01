package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Category;
import com.kiotfpt.model.Shop;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Shop> findByName(String name);
}
