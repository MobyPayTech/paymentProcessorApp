package com.mobpay.Payment.controller;

import com.mobpay.Payment.Encryption.AES;
import com.mobpay.Payment.Encryption.SHA;
import com.mobpay.Payment.Helper.PaymentValidation;
import com.mobpay.Payment.Repository.PaymentRequestEntityRepository;
import com.mobpay.Payment.Repository.PaymentResponseEntityRepository;
import com.mobpay.Payment.Repository.RemoveCardRequestDtoEntityRepository;
import com.mobpay.Payment.Repository.SaveCardDataEntityRepository;
import com.mobpay.Payment.Service.AddCardService;
import com.mobpay.Payment.Service.CurlecPaymentService;
import com.mobpay.Payment.Service.CurlecSubsequentPaymentService;
import com.mobpay.Payment.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class SaveCardController {

    @Autowired
    SaveCardDataEntityRepository saveCardDataEntityRepository;

    @Autowired
    AddCardService addCardService;

    @Autowired
    CurlecSubsequentPaymentService curlecSubsequentPaymentService;

    @Autowired
    PaymentResponseEntityRepository paymentResponseEntityRepository;

    @Autowired
    PaymentRequestEntityRepository paymentrequestentityrepository;

    @Autowired
    RemoveCardRequestDtoEntityRepository removeCardRequestDtoEntityRepository;

    @Autowired
    CurlecPaymentService curlecPaymentService;

    @Autowired
    PaymentValidation paymentValidation;
    
    @Autowired
    SHA sha;

    @RequestMapping(value = "/api/payment/addcard", method = RequestMethod.GET)
    public ModelAndView addCard(@RequestParam(value = "serviceName") String serviceName,
                                @RequestParam(value = "loginId") String loginId, @RequestParam(value = "mobileNo") String mobileNo,
                                @RequestParam(value = "customerName") String customerName, @RequestParam(value = "nameOnCard", required = false) String nameOnCard,
                                @RequestParam(value = "hostType") int hostType, @RequestParam(value = "ip") String ip,
                                @RequestParam(value = "postalCode", required = false) int postalCode, @RequestParam(value = "shippingState", required = false) String shippingState,
                                @RequestParam(value = "billCode") String billCode, @RequestParam(value = "carddetails") String carddetails,
                                @RequestParam(value = "callBackUrl") String callBackUrl, @RequestParam(value = "response", required = false) String response,
                                @RequestParam(value = "latitude", required = false) String latitude, @RequestParam(value = "longitude", required = false) String longitude,
                                @RequestParam(value = "sessionId") String sessionId, @RequestParam(value = "ref1", required = false) String ref1,
                                @RequestParam(value = "ref2", required = false) String ref2,
                                @RequestParam(value = "merchantId") String merchantId, @RequestParam(value = "cardBrand", required = false) String cardBrand,
                                @RequestParam(value = "cardType", required = false) String cardType, @RequestParam(value = "custId", required = false) String custId,
                                @RequestParam(value = "clientType", required = false) int clientType,
                                @RequestParam(value= "amount", required = true) float amount,
                                @RequestParam(value= "sig", required = true) String sig) throws Exception {

        log.info("Save card API invoked by merchant Id "+merchantId+"for customer Id"+loginId);


        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setServiceName(serviceName);
        paymentRequest.setLoginId(loginId);
        paymentRequest.setMobileNo(mobileNo);
        paymentRequest.setCustomerName(customerName);
        paymentRequest.setNameOnCard(nameOnCard);
        paymentRequest.setHostType(hostType);
        paymentRequest.setIp(ip);
      //  paymentRequest.setPostalCode(postalCode);
        paymentRequest.setShippingState(shippingState);
        paymentRequest.setBillCode(billCode);
        paymentRequest.setCarddetails(carddetails);
        paymentRequest.setCallBackUrl(callBackUrl);
        paymentRequest.setResponse(response);
        paymentRequest.setLatitude(latitude);
        paymentRequest.setLongitude(longitude);
        paymentRequest.setSessionId(sessionId);
        paymentRequest.setRef1(ref1);
        paymentRequest.setRef2(ref2);

        paymentRequest.setMerchantId(merchantId);
        paymentRequest.setCardBrand(cardBrand);
        paymentRequest.setCardType(cardType);
        paymentRequest.setCardType(custId);

        paymentRequest.setClientType(clientType);


        String cardetail = paymentRequest.getCarddetails().trim();
        String secretkey = merchantId;
        String decryptedString = AES.decrypt(carddetails, merchantId);
        paymentRequest.setCarddetails(decryptedString);
        System.out.print("decryptedString " +decryptedString);
        log.info("Save card request for session "+sessionId+ " : "+paymentRequest.toString());
        String redirectUrl ="";
        boolean sigFailure = false;
        String sigValue = sha.performSHA(merchantId,billCode,loginId,mobileNo,decryptedString,hostType,clientType,amount);
		if (sig != null && sigValue != null && sig.equals(sigValue)) {
			System.out.print("validation for hash sig is successful");
       
		//	paymentValidation.validateRequestParamsForPayment(loginId, mobileNo, merchantId, decryptedString.split("#")[3]);
			paymentValidation.validateRequestParamsForPayment(loginId, mobileNo, merchantId, amount);
			sigFailure = true;
       
		 redirectUrl = curlecPaymentService.CurlecResponse(paymentRequest,merchantId, "");
		}else {
			System.out.print("401 Authentication Failure");
			log.error("401 Authentication Failure");
		}

        log.info("Payment redirectional url for session "+sessionId+ " : "+redirectUrl);


        if (redirectUrl != null) {

            String cardReference = null;
            try {
                cardReference = Files.readString(Paths.get("/var/tmp/referenceNumber.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            paymentRequest.setCardRef(cardReference);

            saveRequestToDB(paymentRequest);

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setMid(merchantId);
         //   paymentResponse.setCardNo(decryptedString.split("#")[0]);
            paymentResponse.setCardHolderName(nameOnCard);
            paymentResponse.setAmount(amount);
            paymentResponse.setResponseDescription("Payment Approved - Card added successfully");
            paymentResponse.setBillCode(billCode);
            paymentResponse.setResponseCode("00");
            paymentResponse.setResponseMessage("Success");
            paymentResponse.setTokenId(sessionId);
            paymentResponse.setCardBrand(cardBrand);
            paymentResponse.setCardType(cardType);
            paymentResponse.setCardReference(cardReference);

            log.info("Save card response for session "+sessionId+ " : "+paymentResponse.toString());

            saveResponseToDB(paymentResponse);
            log.info("Add payment card with card reference :"+cardReference);

            addCardService.saveCard(paymentRequest, "1", "ACTIVE");

            log.info("Awaiting callback for session :"+sessionId);

            return new ModelAndView("redirect:" + redirectUrl);
        }
        else if(sigFailure == false) {
        	PaymentResponse paymentResponse = new PaymentResponse();

            paymentResponse.setResponseDescription("Authentication failed");
            paymentResponse.setResponseCode("401");
            paymentResponse.setResponseMessage("Authentication failed");

        }
        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setResponseDescription("Payment Transaction Failed");
        paymentResponse.setResponseCode("01");
        paymentResponse.setResponseMessage("Error in processing request");

        log.info("Payment response for session "+sessionId+ " : "+paymentResponse.toString());


        saveResponseToDB(paymentResponse);

        return new ModelAndView("redirect:" + redirectUrl);

    }

    private void saveRequestToDB(PaymentRequest paymentRequest) {
        paymentRequest.setServiceName(paymentRequest.getServiceName());
        paymentRequest.setLoginId(paymentRequest.getLoginId());
        paymentRequest.setMobileNo(paymentRequest.getMobileNo());
        paymentRequest.setCustomerName(paymentRequest.getCustomerName());
        paymentRequest.setNameOnCard(paymentRequest.getNameOnCard());
        paymentRequest.setHostType(paymentRequest.getHostType());
        paymentRequest.setIp(paymentRequest.getIp());
        paymentRequest.setPostalCode(paymentRequest.getPostalCode());
        paymentRequest.setShippingState(paymentRequest.getShippingState());
        paymentRequest.setBillCode(paymentRequest.getBillCode());
        paymentRequest.setCarddetails(paymentRequest.getCarddetails());
        paymentRequest.setCallBackUrl(paymentRequest.getCallBackUrl());
        paymentRequest.setResponse(paymentRequest.getResponse());
        paymentRequest.setLatitude(paymentRequest.getLatitude());
        paymentRequest.setLongitude(paymentRequest.getLongitude());
        paymentRequest.setSessionId(paymentRequest.getSessionId());
        paymentRequest.setRef1(paymentRequest.getRef1());
        paymentRequest.setRef2(paymentRequest.getRef2());
        paymentRequest.setCreatedAt(new Date());
        paymentRequest.setUpdatedAt(new Date());
        paymentRequest.setClientType(paymentRequest.getClientType());
        paymentrequestentityrepository.save(paymentRequest);

    }

    private void saveResponseToDB(PaymentResponse paymentResponse) {
        PaymentResponse paymentResponseToDb = new PaymentResponse();
        paymentResponseToDb.setResponseCode(paymentResponse.getResponseCode());
        paymentResponseToDb.setResponseMessage(paymentResponse.getResponseMessage());
        paymentResponseToDb.setResponseDescription(paymentResponse.getResponseDescription());
        paymentResponseToDb.setAmount(paymentResponse.getAmount());
        paymentResponseToDb.setCardBrand(paymentResponse.getCardBrand());
        paymentResponseToDb.setCardHolderName(paymentResponse.getCardHolderName());
       // paymentResponseToDb.setCardNo(paymentResponse.getCardNo());
        paymentResponseToDb.setCardType(paymentResponse.getCardType());
        paymentResponseToDb.setMid(paymentResponse.getMid());
        paymentResponseToDb.setOrderId(paymentResponse.getOrderId());
        paymentResponseToDb.setTokenId(paymentResponse.getTokenId());
        paymentResponseToDb.setCreatedAt(new Date());
        paymentResponseToDb.setUpdatedAt(new Date());
        paymentResponseToDb.setDatetime(new Date().toString());
        paymentResponseToDb.setCardReference(paymentResponse.getCardReference());
        paymentResponseEntityRepository.save(paymentResponseToDb);
    }


    @PostMapping(value = "/api/payment/removecard")
    public ResultDto removeCard(@Valid @RequestBody RemoveCardRequestDto removeCardRequestDto) {

        log.info("Remove card api invoke by customer "+removeCardRequestDto.getCustId()+"for card reference "+removeCardRequestDto.getCardReference());

        paymentValidation.validateRequestParamsForRemoveCard(removeCardRequestDto.getCustId(),removeCardRequestDto.getMobileNo(),removeCardRequestDto.getCardReference());
        removeCardRequestDto.setCreatedAt(new Date());
        removeCardRequestDto.setUpdatedAt(new Date());
        removeCardRequestDtoEntityRepository.save(removeCardRequestDto);
        try {
            List<SaveCardData> cardData = saveCardDataEntityRepository.findByCustIdAndCardRef(removeCardRequestDto.getCustId(), removeCardRequestDto.getCardReference());
            SaveCardData card = cardData.get(0);
            card.setCardStatus("DEACTIVE");
            card.setDeletedDate(new Date());
            saveCardDataEntityRepository.save(card);
            log.info("Deactivate card reference :"+removeCardRequestDto.getCardReference());
        } catch (Exception e) {
            log.error("Error while deeactivating card reference :"+removeCardRequestDto.getCardReference()+" due to "+e.getLocalizedMessage());
            ResultDto resultDto = ResultDto.builder()
                    .responseCode("01")
                    .responseDescription(e.getLocalizedMessage())
                    .build();
            return resultDto;
        }
        ResultDto resultDto = ResultDto.builder()
                .responseCode("00")
                .responseDescription("Sucessfully removed entry :" + removeCardRequestDto.getCardReference())
                .build();

        return resultDto;

    }
    
    @PostMapping(value = "/api/payment/savecard")
	public Object saveCardDetails(@RequestBody SaveCardRequest saveCardRequest) throws Exception {
		log.info("Inside initpayment");
		String res;
		
		if(saveCardRequest.getHostType()== 1) {
			// paymentValidation.validateRequestParamsForPayment(saveCardRequest.getLoginId(), saveCardRequest.getMobileNo(), saveCardRequest.getMerchantId(), saveCardRequest.getAmount());
			//ResponseEntity<String> response = curlecPaymentService.callCurlecService(saveCardRequest,saveCardRequest.getMerchantId()) ;
		}
		
		Curlec_MandateResponse initResponse = new Curlec_MandateResponse();
		/*
		//initResponse.setAmount(initPayment.getAmount());
		initResponse.setBillCode(initPayment.getBillCode());
		initResponse.setId(initPayment.getId());
		initResponse.setMerchantId(initPayment.getMerchantId());
		initResponse.setResponseCode("00");
		initResponse.setResponseDesc("SUCCESS");
		initResponse.setResponseMessage("SUCCESS");
		initResponse.setTxnID("DAA-22-89");
		*/
		return initResponse;
	}
}
