package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

	Follow findByShopIdAndAccountId(int shopId, int accountId);

}
