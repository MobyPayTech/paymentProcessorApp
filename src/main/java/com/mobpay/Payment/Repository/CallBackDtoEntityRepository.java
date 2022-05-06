package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.CallBackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallBackDtoEntityRepository extends JpaRepository<CallBackDto, Integer > {
}
