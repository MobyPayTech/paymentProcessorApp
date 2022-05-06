package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.CurlecSubsequentPaymentRequest;
import com.mobpay.Payment.dao.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurlecSubsequentPaymentRequestEntityRepository extends JpaRepository<CurlecSubsequentPaymentRequest, Integer > {
}
