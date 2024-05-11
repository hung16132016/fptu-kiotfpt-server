package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Section;

@Repository
public interface AccessibilityItemRepository extends JpaRepository<AccessibilityItem, Integer> {

	List<AccessibilityItem> findBySection(Section section);
}
