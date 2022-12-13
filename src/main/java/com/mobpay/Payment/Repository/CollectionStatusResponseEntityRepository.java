package com.mobpay.Payment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.CollectionStatusResponse;



@Repository
public interface CollectionStatusResponseEntityRepository extends JpaRepository<CollectionStatusResponse, Integer > {
	
	@Query( value = "SELECT * from pp_curlec_collectionStatusResponse p where p.cc_transaction_id = :cc_transaction_id ", nativeQuery = true)
	List<CollectionStatusResponse> findCollectionStatusbyccTransactionId(@Param("cc_transaction_id") String string);
	
}