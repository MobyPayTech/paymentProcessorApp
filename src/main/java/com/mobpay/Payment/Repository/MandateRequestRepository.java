package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.MobiversaSaveCardPaymentResponse;


@Repository
public interface MandateRequestRepository extends JpaRepository<InitMandate, Integer > {
	
}
