package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Account;
import com.kiotfpt.model.Notify;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer> {
	List<Notify> findAllByAccount(Account acc);
}
