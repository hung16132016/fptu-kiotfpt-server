package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kiotfpt.model.AccessibilityItem;
import com.kiotfpt.model.Section;

@Repository
public interface AccessibilityItemRepository extends JpaRepository<AccessibilityItem, Integer> {

	List<AccessibilityItem> findBySection(Section section);
    Optional<AccessibilityItem> findByVariantIdAndSectionId(int variantId, int sectionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AccessibilityItem ai WHERE ai.section.id = :sectionId")
    void deleteBySectionId(@Param("sectionId") int sectionId);
}
