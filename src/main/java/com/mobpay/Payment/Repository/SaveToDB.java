package com.mobpay.Payment.Repository;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.mobpay.Payment.APIKeyAuthFilter;
import com.mobpay.Payment.dao.ChargeUserRequest;
import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.CollectionStatusResponse;
import com.mobpay.Payment.dao.Curlec_MandateResponse;
import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.PaymentLogs;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaveToDB {
	

    @Autowired
    PaymentRequestEntityRepository paymentrequestentityrepository;
    
    @Autowired
    PaymentResponseEntityRepository paymentresponseentityrepository;
    
    @Autowired
    ChargeUserRequestEntityRepository chargeUserRequestEntityRepository;
    
    @Autowired
    ChargeUserResponseEntityRepository chargeUserResponseEntityRepository;
    
    @Autowired
    PaymentResponseEntityRepository paymentResponseEntityRepository;
    
    @Autowired
    CollectionStatusRequestEntityRepository collectionStatusRequestEntityRepository;
    
    @Autowired
    CollectionStatusResponseEntityRepository collectionStatusResponseEntityRepository;
    
    @Autowired
    InitMandateRequestEntityRepository initMandateRequestEntityRepository;
    
    @Autowired
    InitMandateResponseEntityRepository initMandateResponseEntityRepository;
    
    @Autowired
    PaymentLogRepository paymentLogRepository;
    
    @Autowired
	PaymentProcessorAuthRepository paymentProcessorAuthRepository;
	  
    public void saveRequestToDB(PaymentRequest paymentRequest) {
        paymentRequest.setCreatedAt(new Date());
        paymentRequest.setUpdatedAt(new Date());
        paymentrequestentityrepository.save(paymentRequest);
    }

    public void saveRequestToDB(ChargeUserRequest chargeUserRequest) {
    	chargeUserRequest.setCreatedAt(new Date());
    	chargeUserRequest.setUpdatedAt(new Date());
        chargeUserRequestEntityRepository.save(chargeUserRequest);
    }
    
    public void saveResponseToDB(ChargeUserResponse paymentResponse) {
    	paymentResponse.setCreatedAt(new Date());
    	paymentResponse.setUpdatedAt(new Date());
    	chargeUserResponseEntityRepository.save(paymentResponse);
    }
    
    public void saveRequestToDB(CollectionStatusRequest collectionStatusRequest) {
    	collectionStatusRequest.setCreatedAt(new Date());
    	collectionStatusRequest.setUpdatedAt(new Date());
    	collectionStatusRequestEntityRepository.save(collectionStatusRequest);

    }

    public void saveResponseToDB(CollectionStatusResponse collectionStatusResponse) {
    	collectionStatusResponse.setCreatedAt(new Date());
    	collectionStatusResponse.setUpdatedAt(new Date());
    	collectionStatusResponseEntityRepository.save(collectionStatusResponse);
    }
    
    
    public void saveResponseToDB(PaymentResponse paymentResponse) {
    	paymentResponse.setCreatedAt(new Date());
    	paymentResponse.setUpdatedAt(new Date());
    	paymentresponseentityrepository.save(paymentResponse);
    }

    public void saveRequestToDB(InitMandate initMandate) {
    	initMandate.setCreatedAt(new Date());
    	initMandate.setUpdatedAt(new Date());
    	initMandateRequestEntityRepository.save(initMandate);
    }
    
    public void saveResponseToDB(Curlec_MandateResponse initResponse) {
    	initResponse.setCreatedAt(new Date());
    	initResponse.setUpdatedAt(new Date());
    	initMandateResponseEntityRepository.save(initResponse);
    }
    
   /* public void saveRequestToDB(PaymentLogs paymentLogs) {
    	String ipAddress = null;
    	String clientName = null;
    	String key = APIKeyAuthFilter.setKeyAndValue().get("headerKey");
    	try {
    		InetAddress inetAddress = InetAddress.getLocalHost();
    		ipAddress = inetAddress.getHostAddress();
    		}catch(Exception e) {
    			log.info("Exception " +e);
    		}
    	log.info("key " +key);
    	clientName = paymentProcessorAuthRepository.findClientNameFromKey(key);
    	paymentLogs.setIp_address(ipAddress);
    	paymentLogs.setClient_id(clientName);
    	log.info("clientName " +clientName);
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	paymentLogs.setTimestamp(timestamp);  
        paymentLogRepository.save(paymentLogs);
    }*/
}
