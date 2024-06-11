package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Integer> {

	Optional<Variant> findByProductIdAndColorIdAndSizeId(int productId, int colorId, int sizeId);
	
}
