package com.mobpay.Payment.Repository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobpay.Payment.dao.ChargeUserRequest;
import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.CollectionStatusResponse;
import com.mobpay.Payment.dao.Curlec_MandateResponse;
import com.mobpay.Payment.dao.InitMandate;
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
}
