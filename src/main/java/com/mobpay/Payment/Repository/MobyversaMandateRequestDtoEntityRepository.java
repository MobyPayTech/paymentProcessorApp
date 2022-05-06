package com.mobpay.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobpay.Payment.dao.MobyversaNewMandateRequestDto;
import com.mobpay.Payment.dao.NewMandateRequestDto;

@Repository
public interface MobyversaMandateRequestDtoEntityRepository extends JpaRepository<MobyversaNewMandateRequestDto, Integer >{
}



