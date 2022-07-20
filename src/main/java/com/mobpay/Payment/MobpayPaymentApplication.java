package com.mobpay.Payment;

import com.mobpay.Payment.Encryption.AES;
import com.mobpay.Payment.Repository.SaveCardDataEntityRepository;
import com.mobpay.Payment.dao.SaveCardData;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

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
  	    String value=String.format("%06d", number);
  	 // Calendar dates = Calendar.getInstance();
  	//long millisecondsDate = dates.getTimeInMillis();
  	
  	  String uniqueid=dateFormat.format(date)+value;
  	  System.out.println(uniqueid);   
		
		 String secretkey = String.valueOf("MY02004"); String
		 carddetails="5453010000095323#123#0323#125.00";
		// "6250947000000014#123#2612#125.00"; 
		 String encryptedString = AES.encrypt(carddetails,secretkey);
		  System.out.println("Encrypt: "+encryptedString); String decryptedString
		  =AES.decrypt(encryptedString, secretkey);
		 System.out.println("decrypt: "+decryptedString);
		 System.out.println(System.nanoTime());
		

		/*
		 * String secretkey = String.valueOf("447300"); String encryptedString =
		 * AES.encrypt("6250947000000014#123#2612#125.00",secretkey);
		 * System.out.println("Encrypt:"+encryptedString); String decryptedString =
		 * AES.decrypt(
		 * "Tcy4vHLgynRGnBCTyzF7hpWmam1oWnnteQLLu/1Grm9Z16v1s4AXQOLslvXa98Hs".trim(),
		 * secretkey); System.out.println(decryptedString);
		 * System.out.println(decryptedString.split("#")[0]);
		 * System.out.println(decryptedString.split("#")[3]);
		 */
//        String guid = "ddCaOQbLJD+uN2g+s1GLnA==";
		/*
		 * byte[] decoded = Base64.decodeBase64(encryptedString); String hexString =
		 * Hex.encodeHexString(decoded); System.out.println(hexString); byte[] en =
		 * Hex.decodeHex(hexString); String res = Base64.encodeBase64String(en);
		 * System.out.println(res);
		 * 
		 * String decryptedString = AES.decrypt(res, secretkey);
		 * System.out.println(decryptedString);
		 * System.out.println(decryptedString.split("#")[0]);
		 * System.out.println(decryptedString.split("#")[3]);
		 */

	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
