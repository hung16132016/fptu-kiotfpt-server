package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("SELECT p FROM Product p WHERE p.status.id = 11") //haibang
	Page<Product> findByStatusId11(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.status.id = 11 ORDER BY p.id DESC") //haibang
	Page<Product> findLast8ProductsByStatus11(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.status.id = 11 AND p.category.id = :categoryId") //haibang
	Page<Product> findByStatus11AndCategoryId(Pageable pageable, @Param("categoryId") Integer categoryId);
	
	@Query("SELECT p FROM Product p WHERE p.status.id = 11 AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))") //haibang
	Page<Product> findByStatus11AndKeyword(Pageable pageable, @Param("keyword") String keyword);

	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

	@Query("SELECT p FROM Product p WHERE p.popular = true")
    List<Product> findByProductPopularIsTrue();
	
    @Query(value = "SELECT * FROM kiotfpt_product WHERE kiotfpt_product.shop_id = :id",
            countQuery = "SELECT count(*) FROM kiotfpt_product WHERE kiotfpt_product.shop_id = :id",
            nativeQuery = true)
    Page<Product> findAllByShopId(@Param("id") int id, Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.category_id = ?1", nativeQuery = true)
	List<Product> findAllByCategoryid(int id);
	
	@Query(value = "Select * from product where product.name = ?1", nativeQuery = true)
	List<Product> findByname(String name);
	
    List<Product> findByOfficialTrue();

	List<Product> findByDiscountGreaterThan(int discount);
	
}
