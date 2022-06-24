package com.mobpay.Payment.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.MobiversaSaveCardInputPaymentRequest;

@Repository
public interface MobiversaSubPaymentRepository  extends JpaRepository<MobiversaSaveCardInputPaymentRequest, Integer > {
	
}
