package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Accessibility_item;
import com.kiotfpt.model.Section;

@Repository
public interface AccessibilityItemRepository extends JpaRepository<Accessibility_item, Integer> {

	List<Accessibility_item> findBySection(Section section);
}
