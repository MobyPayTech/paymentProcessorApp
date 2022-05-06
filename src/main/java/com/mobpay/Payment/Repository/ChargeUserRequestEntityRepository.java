package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ChargeUserRequest;

@Repository
public interface ChargeUserRequestEntityRepository extends JpaRepository<ChargeUserRequest, Integer > {
}