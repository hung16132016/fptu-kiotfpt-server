package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.AccountProfile;

@Repository
public interface AccountProfileRepository extends JpaRepository<AccountProfile, Integer> {

	Optional<AccountProfile> findByAccount(Account account);

	Optional<AccountProfile> findByAccountId(int accountId);

}
