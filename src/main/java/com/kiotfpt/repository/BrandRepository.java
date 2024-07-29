package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

	Optional<Brand> findByIdAndStatusValue(int brandId, String string);

	Optional<Brand> findByName(String name);

}
