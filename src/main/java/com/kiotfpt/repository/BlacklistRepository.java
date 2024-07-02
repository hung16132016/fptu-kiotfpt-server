package com.kiotfpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiotfpt.model.Blacklist;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    
    boolean existsByTokenAndAccountId(String token, int accountId);

	boolean existsByToken(String token);
}
