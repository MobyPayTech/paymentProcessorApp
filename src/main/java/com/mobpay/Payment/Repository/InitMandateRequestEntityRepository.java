package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.CollectionStatusResponse;
import com.mobpay.Payment.dao.InitMandate;



@Repository
public interface InitMandateRequestEntityRepository extends JpaRepository<InitMandate, Integer > {
}