package com.mobpay.Payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobpay.Payment.Helper.PaymentValidation;
import com.mobpay.Payment.Repository.MobiversaSubPaymentRepository;
import com.mobpay.Payment.Repository.MobiversaSubPaymentResponseRepository;
import com.mobpay.Payment.Repository.PaymentRequestEntityRepository;
import com.mobpay.Payment.Repository.PaymentResponseEntityRepository;
import com.mobpay.Payment.Service.CurlecSubsequentPaymentService;
import com.mobpay.Payment.Service.MobiversaPaymentService;
import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.MobiversaSaveCardInputPaymentRequest;
import com.mobpay.Payment.dao.MobiversaSaveCardPaymentRequest;
import com.mobpay.Payment.dao.MobiversaSaveCardPaymentResponse;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.PaymentResponse;
import com.mobpay.Payment.dao.SubsequentPaymentRequest;
import com.mobpay.Payment.dao.SubsequentPaymentResponse;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import javax.validation.Valid;

import java.util.Date;

@RestController
@Slf4j
public class SubsequentPaymentController {
	 /*
    @Autowired
    CurlecSubsequentPaymentService curlecSubsequentPaymentService;

    @Autowired
    PaymentResponseEntityRepository paymentResponseEntityRepository;

    @Autowired
    PaymentRequestEntityRepository paymentrequestentityrepository;

    @Autowired
    PaymentValidation paymentValidation;
    
   
    @PostMapping("api/payment/subpayment")
    public SubsequentPaymentResponse makeSubsequentPayment(@Valid @RequestBody SubsequentPaymentRequest subsequentPaymentRequest) {

        paymentValidation.validateRequestParamsForSubsequentPayment(subsequentPaymentRequest.getMobileNo(), subsequentPaymentRequest.getMerchantId(),
                subsequentPaymentRequest.getAmount(), subsequentPaymentRequest.getCardReference());

        log.info("Invoke subsequent payment by merchant id " + subsequentPaymentRequest.getMerchantId() + " for card reference " + subsequentPaymentRequest.getCardReference());

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .serviceName(subsequentPaymentRequest.getServiceName())
                .merchantId(subsequentPaymentRequest.getMerchantId())
                .carddetails(subsequentPaymentRequest.getCarddetails())
            //    .amount(subsequentPaymentRequest.getAmount())     //TODO
                .cardRef(subsequentPaymentRequest.getCardReference())
                .mobileNo(subsequentPaymentRequest.getMobileNo())
                .billCode(subsequentPaymentRequest.getBillCode())
                .nameOnCard(subsequentPaymentRequest.getNameOnCard())
                .hostType(subsequentPaymentRequest.getHostType())
                .clientType(subsequentPaymentRequest.getClientType())
                .build();

        saveRequestToDB(paymentRequest);


        SubsequentPaymentResponse subsequentPaymentResponse = curlecSubsequentPaymentService.makeSubsequentPayment(subsequentPaymentRequest);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .responseCode(subsequentPaymentResponse.getResponseCode())
                .responseDescription(subsequentPaymentResponse.getResponseDescription())
                .responseMessage(subsequentPaymentResponse.getResponseDescription())
                .billCode(subsequentPaymentResponse.getBillCode())
                .transactionId(subsequentPaymentResponse.getTransactionId())
                .amount(subsequentPaymentResponse.getAmount())
                .datetime(subsequentPaymentResponse.getDateTime())
                .build();

        log.info("Subsequent payment for card reference " + subsequentPaymentRequest.getCardReference() + " for transaction id" + subsequentPaymentResponse.getTransactionId());
        saveResponseToDB(paymentResponse);

        subsequentPaymentResponse.setId(null);
        return subsequentPaymentResponse;
    }
    
      private void saveRequestToDB(PaymentRequest paymentRequest) {
        paymentRequest.setCreatedAt(new Date());
        paymentRequest.setUpdatedAt(new Date());
        paymentrequestentityrepository.save(paymentRequest);

    }

    private void saveResponseToDB(PaymentResponse paymentResponse) {
        paymentResponse.setCreatedAt(new Date());
        paymentResponse.setUpdatedAt(new Date());
        paymentResponseEntityRepository.save(paymentResponse);
    }
    
    
    
*/
	
	 @Autowired
	    MobiversaPaymentService mobiversaService;
	 
	   
	    @Autowired
	    MobiversaSubPaymentRepository subPaymentRepository;
	 
	    @Autowired
	    MobiversaSubPaymentResponseRepository subPaymentResponseRepository;
	    
	   
  
    private void saveRequestToDB(MobiversaSaveCardInputPaymentRequest paymentRequest) {
        paymentRequest.setCreatedAt(new Date());
        paymentRequest.setUpdatedAt(new Date());
        subPaymentRepository.save(paymentRequest);

    }

    private void saveResponseToDB(MobiversaSaveCardPaymentResponse paymentResponse) {
        paymentResponse.setCreatedAt(new Date());
        paymentResponse.setUpdatedAt(new Date());
        subPaymentResponseRepository.save(paymentResponse);
    }
    
    @PostMapping(value = "/api/payment/mobiversapayment")
	public Object callMobiversaSaveCard(@RequestBody MobiversaSaveCardInputPaymentRequest inputRequest) throws Exception {
		log.info("Inside mobiversapayment" +inputRequest);
		MobiversaSaveCardPaymentResponse paymentResponse = new MobiversaSaveCardPaymentResponse();
		ResponseEntity<String> response = null;
		ObjectMapper mapper = new ObjectMapper();
		String res;
		try {
			saveRequestToDB(inputRequest);
		if(inputRequest.getWalletId() == null || inputRequest.getMobileNo() == null || inputRequest.getFrequency() == null
				|| inputRequest.getBillCode() == null  || inputRequest.getAmount() == null ){
			log.info("Mandatory value is empty..!! ");
			paymentResponse.setResponseCode("01");
			paymentResponse.setResponseMsg("Bad Request ");

		}
		else  {
			response = mobiversaService.callMobiversaSaveCardPaymentService(inputRequest) ;
			log.info("Response from Mobiversa service " +response);
			String body = response.getBody();
			JSONObject bodyJSON = new JSONObject(body);
			log.info("bodyJSON " + bodyJSON);
			Object responseObj = bodyJSON.get("responseData");
			log.info("responseObj " + responseObj);
			JSONObject responseDataJSON = new JSONObject(responseObj.toString());
		if (response != null && response.getStatusCodeValue() == 200 && bodyJSON.getString("responseCode").equals("0000")) {
		
			
			
			paymentResponse.setResponseCode(responseDataJSON.get("responseCode").toString());
			paymentResponse.setResponseMsg(bodyJSON.getString("responseDescription"));
			if(responseDataJSON.get("trxId").toString() != null) {
			paymentResponse.setTrxId(responseDataJSON.get("trxId").toString());
			paymentResponse.setAmount(Float.parseFloat(responseDataJSON.get("amount").toString()));
			
			paymentResponse.setTid(responseDataJSON.get("tid").toString());
			paymentResponse.setMid(responseDataJSON.get("mid").toString());
			
			paymentResponse.setRrn(responseDataJSON.get("rrn").toString());
			paymentResponse.setDate(responseDataJSON.get("date").toString());
			paymentResponse.setTime(responseDataJSON.get("time").toString());
			paymentResponse.setApproveCode(responseDataJSON.get("approveCode").toString());
			paymentResponse.setCardNo(responseDataJSON.get("cardNo").toString());
			paymentResponse.setCardHolderName(responseDataJSON.get("cardHolderName").toString());
			paymentResponse.setTxnType(responseDataJSON.get("txnType").toString());
			paymentResponse.setInvoiceId(responseDataJSON.get("invoiceId").toString());
			}
			 log.info("paymentResponse " + paymentResponse);
		}
		 else if(response != null) {
			paymentResponse.setResponseCode("03");
			paymentResponse.setResponseMsg( bodyJSON.get("responseMessage").toString());
		}
		}saveResponseToDB(paymentResponse);
		}
		catch(InternalServerError e ) {
			log.error("Exception in init payment" + e);
			paymentResponse.setResponseCode("01");
			paymentResponse.setResponseMsg("FAILURE");
		}
		catch(HttpServerErrorException e ) {
			log.error("Exception in init payment" + e);
			paymentResponse.setResponseCode("03");
			paymentResponse.setResponseMsg("Internal Server Error ");
		}
		catch(Exception e ) {
			log.error("Exception in init payment" + e);
			paymentResponse.setResponseCode("03");
			paymentResponse.setResponseMsg("Internal Server Error ");
		}
	
		return paymentResponse;
	}
}
