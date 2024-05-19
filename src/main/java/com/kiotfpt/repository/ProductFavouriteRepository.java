package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.ProductFavourite;

@Repository
public interface ProductFavouriteRepository extends JpaRepository<ProductFavourite, Integer>{
    List<ProductFavourite> findByAccount(Account account);

}
