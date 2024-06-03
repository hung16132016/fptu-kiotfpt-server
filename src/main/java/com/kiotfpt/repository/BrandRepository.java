package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

}
