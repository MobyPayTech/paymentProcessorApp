package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ChargeUserResponse;



@Repository
public interface ChargeUserResponseEntityRepository extends JpaRepository<ChargeUserResponse, Integer > {
}