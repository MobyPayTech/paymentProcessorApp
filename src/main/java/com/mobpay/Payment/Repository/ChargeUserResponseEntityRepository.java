package com.mobpay.Payment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ChargeUserResponse;



@Repository
public interface ChargeUserResponseEntityRepository extends JpaRepository<ChargeUserResponse, Integer > {
	
	@Query( value = "SELECT * from pp_curlec_chargeUserResponse p where p.billCode = :billCode ", nativeQuery = true)
	List<ChargeUserResponse> findCollectionStatusbyccTransactionId(@Param("billCode") String string);
	
}