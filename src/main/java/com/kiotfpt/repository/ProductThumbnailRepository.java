package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.ProductThumbnail;

@Repository
public interface ProductThumbnailRepository extends JpaRepository<ProductThumbnail, Integer>{

}
