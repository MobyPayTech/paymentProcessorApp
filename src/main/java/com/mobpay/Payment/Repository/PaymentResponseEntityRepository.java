package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentResponseEntityRepository extends JpaRepository<PaymentResponse, Integer > {
}


