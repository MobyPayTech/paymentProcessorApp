package com.mobpay.Payment.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.PaymentRequest;

@Repository
public interface PaymentRequestEntityRepository extends JpaRepository<PaymentRequest, Integer > {
	
	@Query("SELECT p from PaymentRequest p WHERE p.uniqueId= :uniqueId")
	PaymentRequest findbyorderId(@Param("uniqueId") String uniqueId);
}
