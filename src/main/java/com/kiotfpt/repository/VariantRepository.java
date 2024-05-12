package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Variant;




@Repository
public interface VariantRepository extends JpaRepository<Variant, Integer> {

}
