package com.kiotfpt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.District;
import com.kiotfpt.model.Province;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer>{

	List<District> findAllByProvince(Province province);
}
