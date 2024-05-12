package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

}
