package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.PaymentLogs;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLogs, Integer > {
}