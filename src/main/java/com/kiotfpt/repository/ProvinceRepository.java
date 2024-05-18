package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer>{

}
