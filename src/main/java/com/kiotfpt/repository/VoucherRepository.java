package com.kiotfpt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Shop;
import com.kiotfpt.model.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
	
	List<Voucher> findAllByShop(Shop shop);

	Optional<Voucher> findByIdAndStatusValue(int id, String string);
}
