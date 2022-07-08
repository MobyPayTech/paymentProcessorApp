package com.mobpay.Payment;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobpay.Payment.Repository.PaymentProcessorConfigRepository;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DbConfig {

	@Autowired
	PaymentProcessorConfigRepository paymentProcessorConfigRepository;
	
	@Bean
	public HashMap<String,String> getValueFromDB() {
		HashMap<String,String> dbValuesMap = new HashMap<String,String>();
		List<PaymentProcessorsysconfig> configValues = paymentProcessorConfigRepository.findAll();
		for (int i=0 ;i<configValues.size(); i++) {
			dbValuesMap.put(configValues.get(i).getName(),configValues.get(i).getValue());
		}
		
		//dbValuesMap.put("aws.s3.bucket" ,creditScoreConfigRepository.findValueFromName("aws.s3.bucket"));
		
		return dbValuesMap;
	}
}
