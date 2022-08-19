package com.mobpay.Payment;

import com.mobpay.Payment.Encryption.AES;
import com.mobpay.Payment.Repository.SaveCardDataEntityRepository;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;
import com.mobpay.Payment.dao.SaveCardData;

import redis.clients.jedis.Jedis;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@SpringBootApplication
public class MobpayPaymentApplication {
	
	

	public static void main(String[] args) throws DecoderException {

		SpringApplication.run(MobpayPaymentApplication.class, args);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String value = String.format("%06d", number);
		String uniqueid = dateFormat.format(date) + value;
		System.out.println(uniqueid);
		String secretkey = String.valueOf("MY02004");
		String carddetails = "5453010000095323#123#0323#125.00";
		String encryptedString = AES.encrypt(carddetails, secretkey);
		System.out.println("Encrypt: " + encryptedString);
		String decryptedString = AES.decrypt(encryptedString, secretkey);
		System.out.println("decrypt: " + decryptedString);
		System.out.println(System.nanoTime());
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
}
