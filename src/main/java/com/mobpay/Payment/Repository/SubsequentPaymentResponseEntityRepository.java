package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.SubsequentPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsequentPaymentResponseEntityRepository extends JpaRepository<SubsequentPaymentResponse, Integer > {
}
