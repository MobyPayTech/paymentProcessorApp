package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.mobpay.Payment.dao.MobiCallBackDto;
import com.mobpay.Payment.dao.PaymentRequest;

@Repository
public interface MobiCallBackDtoEntityRepository extends JpaRepository<MobiCallBackDto, Integer > {

	

}
