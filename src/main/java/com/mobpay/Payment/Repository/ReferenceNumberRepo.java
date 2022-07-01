package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.ReferenceNumber;


@Repository
public interface ReferenceNumberRepo extends JpaRepository<ReferenceNumber, Integer> {
	
	@Query("SELECT p.value from ReferenceNumber p WHERE p.name= :name")
	String findValueByName(@Param("name") String name);
	
}