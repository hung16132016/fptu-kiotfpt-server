package com.kiotfpt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiotfpt.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer>{

    Optional<Status> findByValue(String status_value);
}
