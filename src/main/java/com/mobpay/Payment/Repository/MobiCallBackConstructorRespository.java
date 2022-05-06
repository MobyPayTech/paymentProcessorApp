package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mobpay.Payment.dao.MobiCallBackDto;
import com.mobpay.Payment.dao.MobiCallBackRespositoryDAO;
import com.mobpay.Payment.dao.PaymentRequest;

public interface MobiCallBackConstructorRespository extends JpaRepository<PaymentRequest, Integer >  {
	
}
