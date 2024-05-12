package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

}
