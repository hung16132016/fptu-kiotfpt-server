package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Accessibility_item;

@Repository
public interface AccessibilityItemRepository extends JpaRepository<Accessibility_item, Integer> {

}
