package com.mobpay.Payment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.MobiversaPaymentResponse;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.PaymentResponse;

@Repository
public interface MobiPaymentResponseEntityRepository extends JpaRepository<MobiversaPaymentResponse, Integer > {
	 //Optional<MobiversaPaymentResponse> findByCardReference(String cardReference);
	
}
