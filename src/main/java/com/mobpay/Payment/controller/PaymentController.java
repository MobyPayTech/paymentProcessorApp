package com.mobpay.Payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobpay.Payment.Encryption.SHA;
import com.mobpay.Payment.Helper.PaymentValidation;
import com.mobpay.Payment.Repository.CallBackDtoEntityRepository;
import com.mobpay.Payment.Repository.CollectionStatusRequest;
import com.mobpay.Payment.Repository.MobiCallBackConstructorRespository;
import com.mobpay.Payment.Repository.MobiCallBackDtoEntityRepository;
import com.mobpay.Payment.Repository.MobiPaymentResponseEntityRepository;
import com.mobpay.Payment.Repository.PaymentProcessorConfigRepository;
import com.mobpay.Payment.Repository.PaymentRequestEntityRepository;
import com.mobpay.Payment.Repository.SaveToDB;
import com.mobpay.Payment.Service.AddCardService;
import com.mobpay.Payment.Service.CurlecPaymentService;
import com.mobpay.Payment.Service.CurlecSubsequentPaymentService;
import com.mobpay.Payment.Service.MobiversaPaymentService;
import com.mobpay.Payment.dao.CallBackDto;
import com.mobpay.Payment.dao.ChargeUserRequest;
import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.ChargeUserResponseOutput;
import com.mobpay.Payment.dao.CollectionStatusResponse;
import com.mobpay.Payment.dao.CollectionStatusResponseOutput;
import com.mobpay.Payment.dao.CurlecCallback;
import com.mobpay.Payment.dao.CurlecCallbackResponse;
import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.InitResponse;
import com.mobpay.Payment.dao.InitResponseOutput;
import com.mobpay.Payment.dao.MobiCallBackDto;
import com.mobpay.Payment.dao.MobiversaPaymentResponse;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.PaymentResponse;


import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping(value = "/api/payment/v2.0.0")
public class PaymentController {

    
    @Autowired
    MobiCallBackConstructorRespository mobicallback;

    @Autowired
    CurlecSubsequentPaymentService curlecSubsequentPaymentService;
    
    @Autowired
    MobiPaymentResponseEntityRepository entityRepository;

    @Autowired
    CallBackDtoEntityRepository callBackDtoEntityRepository;
    
    @Autowired
    PaymentRequestEntityRepository paymentrequestentityrepository;
    
    @Autowired
    MobiCallBackDtoEntityRepository mobiCallBackDtoEntityRepository;

    @Autowired
    AddCardService addCardService;

    @Autowired
    CurlecPaymentService curlecPaymentService;

    @Autowired
    PaymentValidation paymentValidation;

    @Autowired
    SHA sha;
    
    @Autowired
    MobiversaPaymentService mobiversaService;
    
    @Autowired
    SaveToDB saveToDB;
    
    @Autowired
	PaymentProcessorConfigRepository paymentProcessorConfigRepository;
	
   // @Value("${payment.callback.url}")
    protected String paymentCallBackUrl; 
    
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> result = null;
  
  List<String> getuniqueId =new ArrayList<String>();

private String merchantCallbackUrl;


    private String genrateUniqueId() {
    	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	  Date date = new Date();
    	  Random rnd = new Random();
    	    int number = rnd.nextInt(999999);
    	    String value=String.format("%06d", number);
    	 // Calendar dates = Calendar.getInstance();
    	//long millisecondsDate = dates.getTimeInMillis();
    	
    	  String uniqueid=dateFormat.format(date)+value;
    	  System.out.println(uniqueid);   
		return uniqueid;
	}


   // @PostMapping(value = "/curleccallback")
  /*  @RequestMapping(value = {"/curleccallback"}, method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getCurlecCallback(@RequestParam Object cc_transaction_id, @RequestParam Object invoice_number,
    		@RequestParam Object collection_status,
    		@RequestParam Object reference_number)
            throws Exception {
    	CurlecCallback curlecCallbackResponse = new CurlecCallback();
    	 log.info("Inside curleccallback " +cc_transaction_id);
    	curlecCallbackResponse.setCcTransactionId(curlecCallback.get("cc_transaction_id").toString())  ;
    	 curlecCallbackResponse.setBillCode(curlecCallback.get("invoice_number").toString())  ;
    	 curlecCallbackResponse.setCollectionStatus(curlecCallback.get("collection_status").toString())  ;
    	 curlecCallbackResponse.setRefNumber(curlecCallback.get("reference_number").toString())  ;
    	 
    	 log.info("curlecCallbackResponse " +curlecCallbackResponse);
    	 
    	 return "POST Response";
    }
    */
    @RequestMapping(value = {"/curleccallback"}, method = RequestMethod.GET)
    public String getCurlecCallbackGet(@RequestParam String cc_transaction_id)
    		/*, @RequestParam Object invoice_number,
    		@RequestParam Object fpx_collectionStatus,
    		@RequestParam Object fpx_sellerOrderNo)*/
            throws Exception {
    	 log.info("Inside curleccallback GET" );
    	 
    	
    	 return "GET Response";
    }
    
    @RequestMapping(value = {"/curleccallback"}, method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public HttpStatus getCurlecCallback(@RequestParam Object cc_transaction_id, @RequestParam Object invoice_number,
    		@RequestParam Object fpx_collectionStatus,
    		@RequestParam Object fpx_sellerOrderNo)
            throws Exception {
    	CurlecCallback curlecCallbackResponse = new CurlecCallback();
    	ResponseEntity<String> responseFromMerchant;
    	 log.info("Inside curleccallback " +fpx_sellerOrderNo + "and  invoice number " +invoice_number);
    	 HttpStatus status = HttpStatus.OK;
    	 /* JSONObject jsonRequest = new JSONObject(curlecCallback.toString());
    	curlecCallbackResponse.setCcTransactionId(jsonRequest.getJSONArray("cc_transaction_id").get(0).toString())  ;
    	 curlecCallbackResponse.setBillCode(jsonRequest.getJSONArray("invoice_number").get(0).toString())  ;
    	 curlecCallbackResponse.setCollectionStatus(jsonRequest.getJSONArray("fpx_collectionStatus").get(0).toString())  ;
    	 curlecCallbackResponse.setRefNumber(jsonRequest.getJSONArray("fpx_sellerOrderNo").get(0).toString())  ;
    	 */
    	 log.info("billCode " +invoice_number.toString().split("-")[0]);
    	 curlecCallbackResponse.setCcTransactionId(cc_transaction_id.toString())  ;
    	 curlecCallbackResponse.setBillCode(invoice_number.toString().split("-")[0]);
    	 curlecCallbackResponse.setInvoiceNumber(invoice_number.toString())  ;
    	 curlecCallbackResponse.setCollectionStatus(fpx_collectionStatus.toString())  ;
    	 curlecCallbackResponse.setRefNumber(fpx_sellerOrderNo.toString())  ;
    	 
    	 log.info("curlecCallbackResponse " +curlecCallbackResponse);
    	 log.info("merchantCallbackUrl " +merchantCallbackUrl);
    	 //TODO remove as it is used for this endpoint testing
    	// merchantCallbackUrl = "https://quocent.com/payment";
    	 responseFromMerchant =  curlecPaymentService.callCurlecCallback(curlecCallbackResponse,merchantCallbackUrl);
    	 log.info("responseFromMerchant " +responseFromMerchant);
    	 return status;
    }
    
    //Mobiversa API call
    @GetMapping(value = "/paymentwithotp")
    public Object processFirstPayment(@RequestParam(value = "serviceName", required = false) String serviceName , @RequestParam(value = "loginId") String loginId,
    		@RequestParam(value = "mobileNo") String mobileNo , @RequestParam(value = "customerName") String customerName,
    		@RequestParam(value = "hostType") int hostType,
    		@RequestParam(value = "ip") String ip , @RequestParam(value = "postalCode", required = false) String postalCode,
    		@RequestParam(value = "shippingState" , required = false) String shippingState , @RequestParam(value = "billCode") String billCode,
    		@RequestParam(value = "amount") float amount , @RequestParam(value ="callBackUrl") String callBackUrl,
    		@RequestParam(value = "redirectUrl") String redirectUrl , @RequestParam(value = "sessionId") String sessionId,
    		@RequestParam(value = "ref1", required = true) String ref1 , @RequestParam(value ="clientType") int clientType,
    		@RequestParam(value = "merchantId") String merchantId , @RequestParam(value ="nameOnCard") String nameOnCard,
    		@RequestParam(value = "creditDetails", required = false) String creditDetails)
            throws Exception {
    	 log.info("Inside paymentwithotp");
    	 PaymentRequest paymentRequest = new PaymentRequest();
    	 PaymentResponse paymentResponse = new PaymentResponse();
    	 ResponseEntity<String> response = null;
	        paymentRequest.setHostType(hostType);
	        paymentRequest.setServiceName(serviceName);
	        paymentRequest.setLoginId(loginId);
	        paymentRequest.setMobileNo(mobileNo);
	        paymentRequest.setCustomerName(customerName);
	        paymentRequest.setIp(ip);
	        paymentRequest.setPostalCode(postalCode);
	        paymentRequest.setShippingState(shippingState);
	        paymentRequest.setBillCode(billCode);
	       // paymentRequest.setAmount(amount);   //TODO change
	        paymentRequest.setCallBackUrl(callBackUrl);
	        log.info("redirectUrl " +redirectUrl);
	        paymentRequest.setRedirectUrl(redirectUrl);
	        log.info("paymentRequest " +paymentRequest.getRedirectUrl());
	        paymentRequest.setSessionId(sessionId);
	        paymentRequest.setRef1(ref1);
	        paymentRequest.setClientType(clientType);
	        paymentRequest.setMerchantId(merchantId);
	        paymentRequest.setNameOnCard(nameOnCard.replace(" ", "%20"));
	      //  paymentRequest.setNameOnCard(URLEncoder.encode(nameOnCard));
	        paymentRequest.setCarddetails(creditDetails);
	        saveToDB.saveRequestToDB(paymentRequest);
	        
	        log.info("paymentRequest " +paymentRequest);
	        //curlec
	      /*  if(paymentRequest.getHostType() == 1) {
	        	log.info("HostType is curlec");
	        	String curlecRequestUrl = curlecPaymentService.callChargeWithOtpUrl(paymentRequest) ;
				log.info("ChargeWithOtpResponse url to hit curlec" +curlecRequestUrl);
				if(curlecRequestUrl != null) {
				//	paymentResponse.setChargeNowWithOtpUrl(curlecRequestUrl);
				} else if(curlecRequestUrl == null){
					paymentResponse.setResponseMessage("FAILURE");
				}
	        }
	        //Mobiversa
	        else*/
	         if(paymentRequest.getHostType() == 2) {
	        	log.info("HostType is Mobiversa");
	        	response = mobiversaService.callMobiversaService(paymentRequest);
	        	log.info("response from  Mobiversa " +response.getBody());
	        	
	        	Document doc = Jsoup.parse(response.getBody());
	        	if(response.getStatusCodeValue() == 200) {
	        	for (Element input : doc.select("input")){
					if (input.attr("name").equalsIgnoreCase("responseDescription")) {
						String responseDescription = input.attr("value");
						if(responseDescription != null)
							paymentResponse.setResponseMessage(responseDescription);
					}
					if (input.attr("name").equalsIgnoreCase("responseCode")) {
						String responseCode = input.attr("value");
						if(responseCode != null && responseCode != "" )
							paymentResponse.setResponseCode(responseCode);
					}
					if (input.attr("name").equalsIgnoreCase("mid")) {
						String mid = input.attr("value");
						if(mid != null && mid != "")
							paymentResponse.setResponseCode(mid);
					}
					if (input.attr("name").equalsIgnoreCase("orderId")) {
						String orderId = input.attr("value");
						if(orderId != null && orderId != "")
							paymentResponse.setBillCode(orderId);
					}
					if (input.attr("name").equalsIgnoreCase("cardHolderName")) {
						String cardHolderName = input.attr("value");
						if(cardHolderName != null && cardHolderName != "")
							paymentResponse.setCardHolderName(cardHolderName);
					}
					if (input.attr("name").equalsIgnoreCase("amount")) {
						if(input.attr("value") != null && input.attr("value") != "") {
						String amountFromMobiversa = input.attr("value") ;
						if(amountFromMobiversa != "0" && amountFromMobiversa != "")
							paymentResponse.setAmount(Float.parseFloat(amountFromMobiversa));
						}
					}
					if (input.attr("name").equalsIgnoreCase("trxId")) {
						String txnId = input.attr("value");
						if(txnId != null && txnId != "")
							paymentResponse.setTransactionId(txnId);
					}
					if (input.attr("name").equalsIgnoreCase("date")) {
						String date = input.attr("value");
						if(date != null && date != "")
							paymentResponse.setDatetime(date);
					}
					if (input.attr("name").equalsIgnoreCase("time")) {
						String time = input.attr("value");
						if(time != null && time	 != "")
							paymentResponse.setDatetime(paymentResponse.getDatetime() +" " + time);
					}
					if (input.attr("name").equalsIgnoreCase("cardNo")) {
						String cardNo = input.attr("value");
						if(cardNo != null && cardNo != "") {
						 paymentResponse.setCardFirstDigits(cardNo.substring(0, 3));
						 paymentResponse.setCardLastDigits(cardNo.substring(cardNo.length() - 4, cardNo.length()));
						}
					}
	        	}paymentResponse.setBillCode(paymentRequest.getBillCode());
	        	saveToDB.saveResponseToDB(paymentResponse);
	        	}else {
	        		for (Element input : doc.select("input")){
	        		if (input.attr("name").equalsIgnoreCase("responseCode")) {
						String responseCode = input.attr("value");
						if(responseCode != null && responseCode != "" )
							paymentResponse.setResponseCode(responseCode);
					}
	        		else 
	        			paymentResponse.setResponseCode("01");
	        		}
	        		paymentResponse.setResponseMessage("FAILURE " + response.getBody());
	        	}
	        }
	        else {
	        	log.info("HostType is invalid");
	        }
	   
        return paymentResponse;
    }

    //Curlec
    @PostMapping(value = "/initmandate")
	public Object callInitMandate(@RequestBody InitMandate initMandate) throws Exception {
		log.info("Inside initpayment" +initMandate);
		saveToDB.saveRequestToDB(initMandate);
		
		InitResponseOutput initResponse = new InitResponseOutput();
		
		try {
		if(initMandate.getEmail() == null || initMandate.getMobileNo() == null ||  initMandate.getNameOnCard() == null ||  initMandate.getIdValue() == null ||
				  initMandate.getClientType() == 0){
			log.info("Mandatory value is empty! ");
			initResponse.setResponseCode("01");
			initResponse.setErrorMsg("Mandatory field not found ");
		}
		else  {
			initResponse = curlecPaymentService.callCurlecNewMandateService(initMandate) ;
			log.info("Response from Curlec service " +initResponse);
		}
		}
		catch(InternalServerError e ) {
			log.error("Exception in init payment" + e);
			initResponse.setResponseCode("01");
			initResponse.setErrorMsg("Internal Server Error");
		}
		catch(HttpServerErrorException e ) {
			log.error("Exception in init payment" + e);
			initResponse.setResponseCode("01");
			initResponse.setErrorMsg("Curlec  Server not reachable");
		}
		return initResponse;
	}
    
    
    //Curlec
    @PostMapping(value = "/charge")
    public Object generateCurlecUrl(@RequestBody ChargeUserRequest chargeUserRequest)
            throws Exception {
    	 log.info("Inside charge ");
    	 ChargeUserResponse paymentResponseDB = new ChargeUserResponse();
    	 ChargeUserResponseOutput paymentResponse = new ChargeUserResponseOutput();
    	 ResponseEntity<String> response = null;
    	/* chargeUserRequest.setAmount(amount);
    	
    	 chargeUserRequest.setRedirectUrl(redirectUrl);
    	 chargeUserRequest.setRef1(refNumber);
    	 chargeUserRequest.setBillCode(billCode);
    	 chargeUserRequest.setWithOTP(withOTP);
    	 */
	        
	        log.info("chargeUserRequest " +chargeUserRequest);
	        merchantCallbackUrl = chargeUserRequest.getCallBackUrl();
	        paymentCallBackUrl = paymentProcessorConfigRepository.findValueFromName("payment.callback.url");
	        log.debug("merchantCallbackUrl " +merchantCallbackUrl);
	        chargeUserRequest.setCallBackUrl(paymentCallBackUrl);
	        log.debug("chargeUserRequest after setting payment callback url " +chargeUserRequest);
	        log.info("WithOtp " +chargeUserRequest.getWithOtp());
	        boolean withotp = chargeUserRequest.getWithOtp();
	        
	        try {
	        if(withotp) {
	        	chargeUserRequest.setCallBackUrl(paymentCallBackUrl);
	        	 saveToDB.saveRequestToDB(chargeUserRequest);
	        	String curlecRequestUrl = curlecPaymentService.callChargeWithOtpUrl(chargeUserRequest) ;
				log.info("ChargeWithOtpResponse url to hit curlec" +curlecRequestUrl);
				if(curlecRequestUrl != null) {
					paymentResponse.setChargeNowWithOtpUrl(curlecRequestUrl);
					paymentResponse.setInvoiceNumber(chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
					paymentResponse.setBillCode(chargeUserRequest.getBillCode()  );
					paymentResponse.setRefNumber(chargeUserRequest.getRefNumber());
					//truncate since it is long
					String curlecUrl = curlecRequestUrl.substring(33);
					paymentResponseDB.setChargeNowWithOtpUrl(curlecUrl);
					paymentResponseDB.setInvoiceNumber(chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo() );
					paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
					paymentResponseDB.setRefNumber(chargeUserRequest.getRefNumber());
				} else if(curlecRequestUrl == null){
					paymentResponse.setErrorMsg("FAILURE");
				}
	        } else if(withotp == false) {
	        	//chargeUserRequest.setCallBackUrl(callBackUrl);
	        	saveToDB.saveRequestToDB(chargeUserRequest);
	        	ResponseEntity<String> chargeWithOtp = curlecPaymentService.callChargeNow(chargeUserRequest) ;
				log.info("ChargeWithResponse url to hit curlec" +chargeWithOtp);
				JSONObject bodyJson = new JSONObject(chargeWithOtp.getBody().toString());
				log.info("bodyJson " + bodyJson);
				JSONObject responseJson = null;
				if(bodyJson.getJSONArray("Status").get(0).toString().equals("200")) {
					log.info("Status code in response is  " + bodyJson.getJSONArray("Status").get(0).toString());
					log.info("Response " + bodyJson.getJSONArray("Response").get(0).toString());
				responseJson = new JSONObject(bodyJson.getJSONArray("Response").get(0).toString())	;			
				log.info("responseJson " + responseJson);
				
				log.info("chargeWithOtp " + chargeWithOtp);
				log.info("Status code " + chargeWithOtp.getStatusCode().toString());
				

				if(chargeWithOtp != null && chargeWithOtp.getStatusCode().toString().contains("200 OK") && bodyJson.getJSONArray("Status").get(0).toString().equals("200")) {
					log.info("Inside Condition Success ");
					paymentResponse.setResponseCode("00");
					paymentResponseDB.setResponseCode("00");
					
										
					if(responseJson.get("collection_status") != null && responseJson.getJSONArray("collection_status").get(0).toString() != null) {
						paymentResponse.setCollection_status(responseJson.getJSONArray("collection_status").get(0).toString());	
						paymentResponseDB.setCollection_status(responseJson.getJSONArray("collection_status").get(0).toString());
					}
					if(responseJson.get("invoice_number") != null && responseJson.getJSONArray("invoice_number").get(0).toString() != null) {
						paymentResponse.setBillCode(chargeUserRequest.getBillCode());
						paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
						paymentResponse.setInvoiceNumber(responseJson.getJSONArray("invoice_number").get(0).toString());
						paymentResponseDB.setInvoiceNumber(responseJson.getJSONArray("invoice_number").get(0).toString());
					}
					if(responseJson.get("cc_transaction_id") != null && responseJson.getJSONArray("cc_transaction_id").get(0).toString() != null) {
						paymentResponse.setCcTransactionId(responseJson.getJSONArray("cc_transaction_id").get(0).toString());
						paymentResponseDB.setCc_transaction_id(responseJson.getJSONArray("cc_transaction_id").get(0).toString());
					}
					if(responseJson.get("reference_number") != null && responseJson.getJSONArray("reference_number").get(0).toString() != null) {
						paymentResponse.setRefNumber(responseJson.getJSONArray("reference_number").get(0).toString());
						paymentResponseDB.setRefNumber(responseJson.getJSONArray("reference_number").get(0).toString());
					}
				}
				}else if(chargeWithOtp != null && chargeWithOtp.getStatusCode().toString().contains("200") && !bodyJson.getJSONArray("Status").get(0).toString().equals("200")){
					log.info("Status code in else loop is  " + bodyJson.getJSONArray("Status").get(0).toString());
					if(!bodyJson.getJSONArray("Message").get(0).toString().isEmpty() && ! bodyJson.getJSONArray("Message").get(0).toString().isBlank() ) {
						paymentResponse.setErrorMsg(bodyJson.getJSONArray("Message").get(0).toString());
						paymentResponse.setResponseCode("01");
					}
					
					else {
						paymentResponse.setErrorMsg("FAILURE");
					paymentResponse.setResponseCode("01");
					paymentResponseDB.setResponseCode("01");
					}
				} else if(chargeWithOtp == null){
					paymentResponse.setErrorMsg("FAILURE");
					paymentResponse.setResponseCode("01");
					paymentResponseDB.setResponseCode("01");
				}
	        }
	        if(chargeUserRequest.getCallBackUrl() != null) {
	        	log.info("Callback is not null" );
	        	
	        }
	        saveToDB.saveResponseToDB(paymentResponseDB);
	        }catch(Exception e) {
	        	paymentResponse.setErrorMsg(e.getLocalizedMessage());
				paymentResponse.setResponseCode("01");
	        }
	        return paymentResponse;
    }
    
    //Curlec
    @PostMapping(value = "/checkcollectionstatus")
	public Object checkCollectionStatus(@RequestBody CollectionStatusRequest collectionStatusRequest) throws Exception {
		log.info("Inside collectionStatusRequest " +collectionStatusRequest);
		CollectionStatusResponse statusResponsedb = new CollectionStatusResponse();
		CollectionStatusResponseOutput statusResponse = new CollectionStatusResponseOutput();
		saveToDB.saveRequestToDB(collectionStatusRequest);
		ResponseEntity<String> response = null;
		ObjectMapper mapper = new ObjectMapper();
		String res;
		log.info("Inside checkcollectionstatus " +collectionStatusRequest);
		try {
		if(collectionStatusRequest.getCcTransactionId()== null || collectionStatusRequest.getClientType() == null ) {
			log.info("Mandatory value is empty..!! ");
			statusResponse.setResponseCode("01");
			statusResponse.setErrorMsg("Mandatory value is empty");
		}
		else  {
			ResponseEntity<String> curlecStatusResponse =	curlecPaymentService.checkCurlecStatus(collectionStatusRequest.getCcTransactionId());
			statusResponse.setResponseCode("00");
			statusResponsedb.setResponseCode("00");
			log.info("Response from curlec collection status " +statusResponse);
			
			if(curlecStatusResponse != null && curlecStatusResponse.getStatusCodeValue() == 200) {
			JSONObject responseJson = new JSONObject(curlecStatusResponse.getBody().toString());
			log.info("responseJson " + responseJson);
			
			if(responseJson.getJSONArray("collection_status").get(0).toString() != null) {
				statusResponse.setCollection_status(responseJson.getJSONArray("collection_status").get(0).toString());	
				statusResponsedb.setCollection_status(responseJson.getJSONArray("collection_status").get(0).toString());
			}
			if(responseJson.getJSONArray("cc_transaction_id").get(0).toString() != null) {
				statusResponse.setCc_transaction_id(responseJson.getJSONArray("cc_transaction_id").get(0).toString());
				statusResponsedb.setCc_transaction_id(responseJson.getJSONArray("cc_transaction_id").get(0).toString());
			}
			if(responseJson.getJSONArray("reference_number").get(0).toString() != null) {
				statusResponse.setCc_transaction_id(responseJson.getJSONArray("reference_number").get(0).toString());
				statusResponsedb.setCc_transaction_id(responseJson.getJSONArray("reference_number").get(0).toString());
			}
			}
			
		}
		saveToDB.saveResponseToDB(statusResponsedb);
		}
		catch(Exception e ) {
			log.error("Exception in init status" + e);
			statusResponse.setResponseCode("01");
			statusResponse.setErrorMsg(e.getLocalizedMessage());
		}
		
		return statusResponse;
	}
    
  
	@RequestMapping(value = "mobicallback", method = { RequestMethod.GET, RequestMethod.POST })
	public void processCallbackTest(@RequestParam("input") String input)
	// @RequestParam("fpx_notes") String fpx_notes)
	{

		System.out.println("Callback:" + input);
		log.info("Callback : +input");
	}

	@PostMapping("/api/payment/callback/new-mandate")
	//@RequestMapping(value = "//api/payment/callback/new-mandate", method = {RequestMethod.GET,RequestMethod.POST})
    public CallBackDto processCallback(@RequestParam("curlec_method") String curlec_method, @RequestParam("fpx_fpxTxnId") String fpx_fpxTxnId,
                                       @RequestParam("fpx_sellerExOrderNo") String fpx_sellerExOrderNo, @RequestParam("fpx_fpxTxnTime") String fpx_fpxTxnTime,
                                       @RequestParam("fpx_sellerOrderNo") String fpx_sellerOrderNo, @RequestParam("fpx_sellerId") String fpx_sellerId,
                                       @RequestParam("fpx_txnCurrency") String fpx_txnCurrency, @RequestParam("fpx_txnAmount") String fpx_txnAmount,
                                       @RequestParam("fpx_buyerName") String fpx_buyerName, @RequestParam("fpx_buyerBankId") String fpx_buyerBankId,
                                       @RequestParam("fpx_debitAuthCode") String fpx_debitAuthCode, @RequestParam("fpx_type") String fpx_type)
                                      // @RequestParam("fpx_notes") String fpx_notes)
                                       {
        CallBackDto callBackDto = CallBackDto.builder()
                .curlec_method(curlec_method)
                .fpx_buyerName(fpx_buyerName)
                .fpx_buyerBankId(fpx_buyerBankId)
                .fpx_debitAuthCode(fpx_debitAuthCode)
             //   .fpx_notes(fpx_notes)
                .fpx_sellerId(fpx_sellerId)
                .fpx_sellerOrderNo(fpx_sellerOrderNo)
                .fpx_txnAmount(fpx_txnAmount)
                .fpx_txnCurrency(fpx_txnCurrency)
                .fpx_type(fpx_type)
                .fpx_fpxTxnId(fpx_fpxTxnId)
                .fpx_fpxTxnTime(fpx_fpxTxnTime)
                .fpx_sellerExOrderNo(fpx_sellerExOrderNo)
                .build();

        System.out.println("Callback data:" + callBackDto.toString());

        CallBackDto res = callBackDtoEntityRepository.save(callBackDto);
        return res;
    }

    
    @PostMapping("/callback/mobypayCallback")
    public String MobyprocessCallback(@RequestBody MobiCallBackDto callBackDto) throws ParseException {
    	
		Map<String,String> mobipaymentResponse=new LinkedHashMap<String,String>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMMM-yyyy");
		 SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
		String datevalue=callBackDto.getDate();
		 Date date = dateFormat.parse(datevalue);
          
		    System.out.println(sdfOut.format(date));
	
		String datewise=sdfOut.format(date);
		  MobiCallBackDto mobicallBackDto =
		  MobiCallBackDto.builder().Aid(callBackDto.getAid()).Amount(callBackDto.getAmount()).ApproveCode(
				  callBackDto.getApproveCode())
		  .CardHolderName(callBackDto.getCardHolderName()).CardNo(callBackDto.getCardNo()).Date(datewise).Mid(callBackDto.getMid()).OrderId(
				  callBackDto.getOrderId())
		  .ResponseCode(callBackDto.getResponseCode()).ResponseDescription(callBackDto.getResponseDescription()).
		  ResponseMessage(callBackDto.getResponseMessage())
		  .Tid(callBackDto.getTid()).Uid(callBackDto.getUid()).WalletId(callBackDto.getWalletId()).Rrn(callBackDto.getRrn()).Time(callBackDto.getTime())
		  .CardType(callBackDto.getCardType()).CardBrand(callBackDto.getCardBrand()).Currency(callBackDto.getCurrency()).CountryCode(callBackDto.getCountryCode()).build();
		  
		  

        System.out.println("Mobi Callback data:" +mobicallBackDto);

       MobiCallBackDto res = mobiCallBackDtoEntityRepository.save(mobicallBackDto);
       if(callBackDto.getResponseMessage().contains("Success")) {
       if( callBackDto.getOrderId()!=null ) {
    	  PaymentRequest paymentRequest=new PaymentRequest();
    	   for(int i=0;i<getuniqueId.size();i++) {
    		String uniquevalue=getuniqueId.get(i);
    	   paymentRequest=paymentrequestentityrepository.findbyorderId(uniquevalue);
    	   String merchantcallback=paymentRequest.getCallBackUrl();
    	   String merchantId=paymentRequest.getMerchantId();
    	   paymentRequest.setCarddetails(callBackDto.getCardNo());
    	   mobipaymentResponse.put("mid", merchantId);
    	   mobipaymentResponse.put("datetime", callBackDto.getDate()+" "+callBackDto.getTime());
    	   mobipaymentResponse.put("cardNo", callBackDto.getCardNo());
    	   mobipaymentResponse.put("cardHolderName",callBackDto.getCardHolderName());
    	   mobipaymentResponse.put("amount", callBackDto.getAmount());
    	   mobipaymentResponse.put("responseDescription", "Payment Approved - Card added successfully");
    	   mobipaymentResponse.put("billCode", paymentRequest.getBillCode());
    	   mobipaymentResponse.put("responseMessage", "Success");
    	   mobipaymentResponse.put("tokenId", callBackDto.getWalletId());
    	   mobipaymentResponse.put("cardBrand", callBackDto.getCardBrand());
    	   mobipaymentResponse.put("cardType", callBackDto.getCardType());
    	   String url=merchantcallback;
    	   
    	   try {
			URI uri = new URI(url);
			 result = restTemplate.postForEntity(uri, mobipaymentResponse,
	                    String.class);
          System.out.println(result.getBody());
          return result.getBody();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   
    	   } 	   
    	  
    	   }else {
    	   System.out.println("OrderId does not Exist");
    	   mobipaymentResponse.put("responseDescription", "Incorrect Input");
    	   mobipaymentResponse.put("responseCode", "02");
    	   mobipaymentResponse.put("responseMessage", "Bad Request");
    	 
       }
       }else {
    	   System.out.println("Not Sucess");
    	   mobipaymentResponse.put("responseDescription", "Payment Transaction Failed");
    	   mobipaymentResponse.put("responseCode", "01");
    	   mobipaymentResponse.put("responseMessage", "Insufficient Funds");  
       }
        return result.getBody();
    }

	
}
