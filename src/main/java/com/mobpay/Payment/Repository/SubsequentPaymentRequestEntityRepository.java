package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.SubsequentPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsequentPaymentRequestEntityRepository extends JpaRepository<SubsequentPaymentRequest, Integer > {
}
