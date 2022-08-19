package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.PaymentProcessorAuthDao;

@Repository
public interface PaymentProcessorAuthRepository extends JpaRepository<PaymentProcessorAuthDao, Integer >{

	@Query( value = "SELECT p.client_name from paymentprocessor_auth p where p.api_key = :api_key", nativeQuery = true)
	 String findClientNameFromKey(@Param("api_key") String string);
}