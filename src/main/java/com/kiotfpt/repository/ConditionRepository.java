package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.ProductCondition;

@Repository
public interface ConditionRepository extends JpaRepository<ProductCondition, Integer> {

}
