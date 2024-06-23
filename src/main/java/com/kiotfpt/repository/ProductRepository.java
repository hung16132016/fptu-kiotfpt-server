package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Category;
import com.kiotfpt.model.Product;
import com.kiotfpt.model.Shop;

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
    
    List<Product> findAllByShop(Shop shop);

	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.category_id = ?1", nativeQuery = true)
	List<Product> findAllByCategoryid(int id);
	
	@Query(value = "Select * from product where product.name = ?1", nativeQuery = true)
	List<Product> findByname(String name);
	
    Page<Product> findByDiscountGreaterThan(int discount, Pageable pageable);
    Page<Product> findByShopIdAndDiscountGreaterThan(int shopId, int discount, Pageable pageable);
	
	@Query(value = "Select kiotfpt_product.category_id, count(*) as product_count from kiotfpt_product group by kiotfpt_product.category_id order by product_count desc limit 4", nativeQuery = true)
	List<Object[]> findTop4PopularCategory();
	
	@Query(value = "Select kiotfpt_product.brand_id, count(*) as product_count from kiotfpt_product group by kiotfpt_product.brand_id order by product_count desc limit 4;", nativeQuery = true)
	List<Object[]> findTop4PopularBrand();
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_top_deal = 1", nativeQuery = true)
	Page<Product> findByTopDeal(Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_top_deal = 1 and kiotfpt_product.shop_id = :shopId", nativeQuery = true)
	Page<Product> findByTopDealAndShopId(@Param("shopId") Integer shopId, Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_best_seller = 1", nativeQuery = true)
	Page<Product> findByBestSeller(Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_best_seller = 1 and kiotfpt_product.shop_id = :shopId", nativeQuery = true)
	Page<Product> findByBestSellerAndShopId(@Param("shopId") Integer shopId, Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_popular = 1", nativeQuery = true)
	Page<Product> findByPopular(Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_popular = 1 and kiotfpt_product.shop_id = :shopId", nativeQuery = true)
	Page<Product> findByPopularAndShopId(@Param("shopId") Integer shopId, Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_official = 1", nativeQuery = true)
	Page<Product> findByOfficial(Pageable pageable);
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.product_official = 1 and kiotfpt_product.shop_id = :shopId", nativeQuery = true)
	Page<Product> findByOfficialAndShopId(@Param("shopId") Integer shopId, Pageable pageable);
	
	@Query(value = "Select distinct brand_id From kiotfpt_product where kiotfpt_product.category_id = :category_id", nativeQuery = true)
	List<Object[]> findBrandByCategory(@Param("category_id") int id);
	
	List<Product> findTop6ByCategoryAndIdNot(Category category, int productId);
	
	@Query(value = "Select * From kiotfpt_product where kiotfpt_product.product_min_price between :min_price and :max_price", nativeQuery = true)
	Page<Product> findByPriceRange(@Param("min_price") float min_price, @Param("max_price") float max_price, Pageable pageable);
	
	@Query(value = "SELECT * FROM kiotfpt_product WHERE kiotfpt_product.status_id = 11", nativeQuery = true) //haibang
	List<Product> findAllActiveProduct();
	
	@Query(value = "Select * from kiotfpt_product where kiotfpt_product.category_id = :cateId and kiotfpt_product.shop_id = :shopId", nativeQuery = true)
	Page<Product> findByShopIDAndCateID(@Param("shopId") Integer shopId, @Param("cateId") Integer cateId, Pageable pageable);
}
