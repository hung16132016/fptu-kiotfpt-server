package com.kiotfpt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.AccountProfile;
import com.kiotfpt.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
	List<Address> findAllByAccountProfile(AccountProfile acc);
}
