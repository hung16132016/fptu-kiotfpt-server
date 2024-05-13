package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
	
	@Query("SELECT b FROM Brand b JOIN FETCH b.products p GROUP BY b ORDER BY COUNT(p) DESC")
	List<Brand> findPopularBrandsWithLimit(Pageable pageable);

}
