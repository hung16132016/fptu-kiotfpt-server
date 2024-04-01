package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query("SELECT p FROM Product p WHERE LOWER(p.product_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

	@Query("SELECT p FROM Product p WHERE p.product_popular = true")
    List<Product> findByProductPopularIsTrue();
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.shop_id = ?1", nativeQuery = true)
	List<Product> findAllByShopid(int id);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.category_id = ?1", nativeQuery = true)
	List<Product> findAllByCategoryid(int id);
	
	@Query(value = "Select * from product where product.name = ?1", nativeQuery = true)
	List<Product> findByname(String name);
	
}
