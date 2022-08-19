package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

@Repository
public interface PaymentProcessorConfigRepository extends JpaRepository<PaymentProcessorsysconfig, Integer >{

	@Query( value = "SELECT p.value from paymentprocessor_sysconfig p where p.name = :name", nativeQuery = true)
	 String findValueFromName(@Param("name") String string);
}
