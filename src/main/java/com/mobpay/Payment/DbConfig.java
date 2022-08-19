package com.mobpay.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.mobpay.Payment.Repository.PaymentProcessorConfigRepository;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DbConfig {

	@Autowired
	PaymentProcessorConfigRepository paymentProcessorConfigRepository;
	
	@Autowired          
    private RedisTemplate<String, PaymentProcessorsysconfig> redisTemplate;
	
	@Bean
	public HashMap<String,String> getValueFromDB() {
		HashMap<String,String> dbValuesMap = new HashMap<String,String>();
		List<PaymentProcessorsysconfig> configValues = paymentProcessorConfigRepository.findAll();
		Map<Integer, PaymentProcessorsysconfig> map = new HashMap<>();
		for (int i=0 ;i<configValues.size(); i++) {
			dbValuesMap.put(configValues.get(i).getName(),configValues.get(i).getValue());
		}
		saveAll(configValues);
		//dbValuesMap.put("aws.s3.bucket" ,creditScoreConfigRepository.findValueFromName("aws.s3.bucket"));
		return dbValuesMap;
	}
	
	public void saveAll(List<PaymentProcessorsysconfig> list) {
		for (PaymentProcessorsysconfig itr : list) {
			redisTemplate.opsForValue().set("paymentProcessor/"+itr.getName(), itr);
		}
		System.out.println(redisTemplate.opsForValue());
    }
	
	public PaymentProcessorsysconfig readValuesFromRedis(String key){
		PaymentProcessorsysconfig paymentProcessorsysconfig = redisTemplate.opsForValue().get("paymentProcessor/"+key);
		return paymentProcessorsysconfig;
	}
}
