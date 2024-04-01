package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Section;
import com.kiotfpt.model.Shop;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	List<Section> findByShop(Shop shop);
}
