package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.Curlec_MandateResponse;


@Repository
public interface InitMandateResponseEntityRepository extends JpaRepository<Curlec_MandateResponse, Integer > {
	
	 @Query("SELECT p.vgsNumber from Curlec_MandateResponse p WHERE p.refNumber= :refNumber")
	  String findByRefNo(@Param("refNumber") String paramString);
	
}