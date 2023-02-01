package com.mobpay.Payment.controller;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobpay.Payment.DbConfig;
import com.mobpay.Payment.EmailUtility;
import com.mobpay.Payment.Encryption.SHA;
import com.mobpay.Payment.Helper.PaymentValidation;
import com.mobpay.Payment.Repository.ChargeUserResponseEntityRepository;
import com.mobpay.Payment.Repository.CollectionStatusRequest;
import com.mobpay.Payment.Repository.CollectionStatusResponseEntityRepository;
import com.mobpay.Payment.Repository.InitMandateResponseEntityRepository;
import com.mobpay.Payment.Repository.MobiCallBackConstructorRespository;
import com.mobpay.Payment.Repository.PaymentRequestEntityRepository;
import com.mobpay.Payment.Repository.QueryStatusRequest;
import com.mobpay.Payment.Repository.SaveToDB;
import com.mobpay.Payment.Service.AddCardService;
import com.mobpay.Payment.Service.CurlecMethod;
import com.mobpay.Payment.Service.CurlecPaymentService;
import com.mobpay.Payment.Service.CurlecPaymentServiceImpl;
import com.mobpay.Payment.Service.CurlecSubsequentPaymentService;
import com.mobpay.Payment.Service.GlobalConstants;
import com.mobpay.Payment.Service.MobiversaPaymentService;
import com.mobpay.Payment.dao.ChargeUserRequest;
import com.mobpay.Payment.dao.ChargeUserResponse;
import com.mobpay.Payment.dao.ChargeUserResponseOutput;
import com.mobpay.Payment.dao.CollectionResponse;
import com.mobpay.Payment.dao.CollectionStatusResponse;
import com.mobpay.Payment.dao.CollectionStatusResponseOutput;
import com.mobpay.Payment.dao.CurlecCallback;
import com.mobpay.Payment.dao.CurlecRequeryResponse;
import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.InitResponseOutput;
import com.mobpay.Payment.dao.PaymentLogs;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.PaymentResponse;
import com.mobpay.Payment.dao.RequeryRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/api/payment/v2.0.0")
public class PaymentController {

	public static Logger logger;

	static {
		try {
			boolean append = true;
			SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
			FileHandler fh = new FileHandler("LogFile_" + format.format(Calendar.getInstance().getTime()) + ".log",
					append);
			fh.setFormatter(new SimpleFormatter());
			logger = Logger.getLogger("LogFile_" + format.format(Calendar.getInstance().getTime()));
			logger.addHandler(fh);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	MobiCallBackConstructorRespository mobicallback;

	@Autowired
	CurlecSubsequentPaymentService curlecSubsequentPaymentService;

	// @Autowired
	// MobiPaymentResponseEntityRepository entityRepository;

	// @Autowired
	// CallBackDtoEntityRepository callBackDtoEntityRepository;

	@Autowired
	PaymentRequestEntityRepository paymentrequestentityrepository;

//    @Autowired
	// MobiCallBackDtoEntityRepository mobiCallBackDtoEntityRepository;

	@Autowired
	AddCardService addCardService;

	@Autowired
	CurlecPaymentServiceImpl curlecPaymentService;

	@Autowired
	PaymentValidation paymentValidation;

	@Autowired
	SHA sha;

	@Autowired
	MobiversaPaymentService mobiversaService;

	@Autowired
	SaveToDB saveToDB;

	@Autowired
	DbConfig dbconfig;

	@Autowired
	InitMandateResponseEntityRepository initMandateResponseEntityRepository;

	@Autowired
	ChargeUserResponseEntityRepository chargeUserResponseRepo;

	@Autowired
	private CurlecPaymentService callBackUrl;

	@Autowired
	private CollectionStatusResponseEntityRepository collectionRepo;
	
	@Autowired 
	private ObjectMapper objectMapper;

	// @Value("${payment.callback.url}")
	protected String paymentCallBackUrl;
	protected String simulator = null;

	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> result = null;

	List<String> getuniqueId = new ArrayList<String>();

//	private String merchantCallbackUrl;

	private String ccTransactionId;

	private String genrateUniqueId() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String value = String.format("%06d", number);
		// Calendar dates = Calendar.getInstance();
		// long millisecondsDate = dates.getTimeInMillis();

		String uniqueid = dateFormat.format(date) + value;
		return uniqueid;
	}

	// @PostMapping(value = "/curleccallback")
	/*
	 * @RequestMapping(value = {"/curleccallback"}, method =
	 * RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	 * public String getCurlecCallback(@RequestParam Object
	 * cc_transaction_id, @RequestParam Object invoice_number,
	 * 
	 * @RequestParam Object collection_status,
	 * 
	 * @RequestParam Object reference_number) throws Exception { CurlecCallback
	 * curlecCallbackResponse = new CurlecCallback();
	 * log.info("Inside curleccallback " +cc_transaction_id);
	 * curlecCallbackResponse.setCcTransactionId(curlecCallback.get(
	 * "cc_transaction_id").toString()) ;
	 * curlecCallbackResponse.setBillCode(curlecCallback.get("invoice_number").
	 * toString()) ; curlecCallbackResponse.setCollectionStatus(curlecCallback.get(
	 * "collection_status").toString()) ;
	 * curlecCallbackResponse.setRefNumber(curlecCallback.get("reference_number").
	 * toString()) ;
	 * 
	 * log.info("curlecCallbackResponse " +curlecCallbackResponse);
	 * 
	 * return "POST Response"; }
	 */
	@ResponseBody
	@RequestMapping(value = {
			"/curleccallback" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public HttpStatus getCurlecCallback(@RequestParam Object cc_transaction_id, @RequestParam Object invoice_number,
			@RequestParam Object fpx_collectionStatus, @RequestParam Object fpx_sellerOrderNo) throws Exception {
		CurlecCallback curlecCallbackResponse = new CurlecCallback();
//		ChargeUserResponse curlecResponse = new ChargeUserResponse();
		List<ChargeUserResponse> curlecResponse = chargeUserResponseRepo
				.findCollectionStatusbyccTransactionId(cc_transaction_id.toString());
		ResponseEntity<String> responseFromMerchant;
		log.info("Inside curleccallback " + fpx_sellerOrderNo + "and  invoice number " + invoice_number);
		HttpStatus status = HttpStatus.OK;
		String res = "";
		if (invoice_number != null) {
			String[] split = invoice_number.toString().split("-");

			for (int i = 0; i < split.length - 1; i++) {
				res += split[i] + "-";
			}
		}
		String billCode = StringUtils.chop(res);
		log.info("billCode " + invoice_number.toString().split("-")[0]);
		curlecCallbackResponse.setCcTransactionId(cc_transaction_id.toString());
		curlecCallbackResponse.setBillCode(billCode);
		curlecCallbackResponse.setInvoiceNumber(invoice_number.toString());
		curlecCallbackResponse.setCollectionStatus(fpx_collectionStatus.toString());
		curlecCallbackResponse.setRefNumber(fpx_sellerOrderNo.toString());
		String curlecCallbackUrl = callBackUrl.getCurlecCallbackUrl(curlecCallbackResponse);

		// Saving to curlec userResponse table
		curlecResponse.get(0).setCcTransactionId(cc_transaction_id.toString());
		curlecResponse.get(0).setBillCode(billCode);
		curlecResponse.get(0).setCollection_status(fpx_collectionStatus.toString());
		curlecResponse.get(0).setRefNumber(fpx_sellerOrderNo.toString());
		curlecResponse.get(0).setMethod(CurlecMethod.CURLEC_CALLBACK_WITHOTP.label);
		curlecResponse.get(0).setInvoiceNumber(invoice_number.toString());
		curlecResponse.get(0).setChargeNowWithOtpUrl(curlecCallbackUrl);
		log.info("curlecCallbackResponse " + curlecCallbackResponse);
		log.info("merchantCallbackUrl " + curlecCallbackUrl);
		logger.info("Inside [PaymentController:getCurlecCallback] - curlecCallbackResponse " + curlecCallbackResponse);
		logger.info("Inside [PaymentController:getCurlecCallback] - merchantCallbackUrl " + curlecCallbackUrl);
		// TODO remove as it is used for this endpoint testing
		// merchantCallbackUrl = "https://quocent.com/payment";
		responseFromMerchant = curlecPaymentService.callCurlecCallback(curlecCallbackResponse, curlecCallbackUrl);
		log.info("responseFromMerchant " + responseFromMerchant);
		logger.info("Inside [PaymentController:getCurlecCallback] - Response From Merchant " + responseFromMerchant);
		curlecResponse.get(0).setResponseCode(status.toString());
		curlecResponse.get(0).setResponseMessage(curlecCallbackResponse.toString());
		saveToDB.saveResponseToDB(curlecResponse.get(0));
		return status;
	}

	// Mobiversa API call
	@ResponseBody
	@GetMapping(value = "/paymentwithotp")
	public Object processFirstPayment(@RequestParam(value = "serviceName", required = false) String serviceName,
			@RequestParam(value = "loginId") String loginId, @RequestParam(value = "mobileNo") String mobileNo,
			@RequestParam(value = "customerName") String customerName, @RequestParam(value = "hostType") int hostType,
			@RequestParam(value = "ip") String ip,
			@RequestParam(value = "postalCode", required = false) String postalCode,
			@RequestParam(value = "shippingState", required = false) String shippingState,
			@RequestParam(value = "billCode") String billCode, @RequestParam(value = "amount") float amount,
			@RequestParam(value = "callBackUrl") String callBackUrl,
			@RequestParam(value = "redirectUrl") String redirectUrl,
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "ref1", required = true) String ref1,
			@RequestParam(value = "clientType") int clientType, @RequestParam(value = "merchantId") String merchantId,
			@RequestParam(value = "nameOnCard") String nameOnCard,
			@RequestParam(value = "creditDetails", required = false) String creditDetails) throws Exception {
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
		// paymentRequest.setAmount(amount); //TODO change
		paymentRequest.setCallBackUrl(callBackUrl);
		log.info("redirectUrl " + redirectUrl);
		paymentRequest.setRedirectUrl(redirectUrl);
		log.info("paymentRequest " + paymentRequest.getRedirectUrl());
		paymentRequest.setSessionId(sessionId);
		paymentRequest.setRef1(ref1);
		paymentRequest.setClientType(clientType);
		paymentRequest.setMerchantId(merchantId);
		paymentRequest.setNameOnCard(nameOnCard.replace(" ", "%20"));
		// paymentRequest.setNameOnCard(URLEncoder.encode(nameOnCard));
		paymentRequest.setCarddetails(creditDetails);
		saveToDB.saveRequestToDB(paymentRequest);

		log.info("paymentRequest " + paymentRequest);
		logger.info("Inside [PaymentController:processFirstPayment] - Payment Request " + paymentRequest);
		// curlec
		/*
		 * if(paymentRequest.getHostType() == 1) { log.info("HostType is curlec");
		 * String curlecRequestUrl =
		 * curlecPaymentService.callChargeWithOtpUrl(paymentRequest) ;
		 * log.info("ChargeWithOtpResponse url to hit curlec" +curlecRequestUrl);
		 * if(curlecRequestUrl != null) { //
		 * paymentResponse.setChargeNowWithOtpUrl(curlecRequestUrl); } else
		 * if(curlecRequestUrl == null){ paymentResponse.setResponseMessage("FAILURE");
		 * } } //Mobiversa else
		 */
		if (paymentRequest.getHostType() == 2) {
			log.info("HostType is Mobiversa");
			response = mobiversaService.callMobiversaService(paymentRequest);
			log.info("response from  Mobiversa " + response.getBody());
			logger.info(
					"Inside [PaymentController:processFirstPayment] - Response from  Mobiversa " + response.getBody());
			Document doc = Jsoup.parse(response.getBody());
			if (response.getStatusCodeValue() == 200) {
				for (Element input : doc.select("input")) {
					if (input.attr("name").equalsIgnoreCase("responseDescription")) {
						String responseDescription = input.attr("value");
						if (responseDescription != null)
							paymentResponse.setResponseMessage(responseDescription);
					}
					if (input.attr("name").equalsIgnoreCase("responseCode")) {
						String responseCode = input.attr("value");
						if (responseCode != null && responseCode != "")
							paymentResponse.setResponseCode(responseCode);
					}
					if (input.attr("name").equalsIgnoreCase("mid")) {
						String mid = input.attr("value");
						if (mid != null && mid != "")
							paymentResponse.setResponseCode(mid);
					}
					if (input.attr("name").equalsIgnoreCase("orderId")) {
						String orderId = input.attr("value");
						if (orderId != null && orderId != "")
							paymentResponse.setBillCode(orderId);
					}
					if (input.attr("name").equalsIgnoreCase("cardHolderName")) {
						String cardHolderName = input.attr("value");
						if (cardHolderName != null && cardHolderName != "")
							paymentResponse.setCardHolderName(cardHolderName);
					}
					if (input.attr("name").equalsIgnoreCase("amount")) {
						if (input.attr("value") != null && input.attr("value") != "") {
							String amountFromMobiversa = input.attr("value");
							if (amountFromMobiversa != "0" && amountFromMobiversa != "")
								paymentResponse.setAmount(Float.parseFloat(amountFromMobiversa));
						}
					}
					if (input.attr("name").equalsIgnoreCase("trxId")) {
						String txnId = input.attr("value");
						if (txnId != null && txnId != "")
							paymentResponse.setTransactionId(txnId);
					}
					if (input.attr("name").equalsIgnoreCase("date")) {
						String date = input.attr("value");
						if (date != null && date != "")
							paymentResponse.setDatetime(date);
					}
					if (input.attr("name").equalsIgnoreCase("time")) {
						String time = input.attr("value");
						if (time != null && time != "")
							paymentResponse.setDatetime(paymentResponse.getDatetime() + " " + time);
					}
					if (input.attr("name").equalsIgnoreCase("cardNo")) {
						String cardNo = input.attr("value");
						if (cardNo != null && cardNo != "") {
							paymentResponse.setCardFirstDigits(cardNo.substring(0, 3));
							paymentResponse.setCardLastDigits(cardNo.substring(cardNo.length() - 4, cardNo.length()));
						}
					}
				}
				paymentResponse.setBillCode(paymentRequest.getBillCode());
				saveToDB.saveResponseToDB(paymentResponse);
			} else {
				for (Element input : doc.select("input")) {
					if (input.attr("name").equalsIgnoreCase("responseCode")) {
						String responseCode = input.attr("value");
						if (responseCode != null && responseCode != "")
							paymentResponse.setResponseCode(responseCode);
					} else
						paymentResponse.setResponseCode("01");
				}
				paymentResponse.setResponseMessage("FAILURE " + response.getBody());
			}
		} else {
			log.info("HostType is invalid");
			logger.severe("Inside [PaymentController:processFirstPayment] - HostType is invalid ");
		}

		return paymentResponse;
	}

	// Curlec
	@ResponseBody
	@PostMapping(value = "/initmandate")
	public Object callInitMandate(@RequestBody InitMandate initMandate) throws Exception {
		log.info("Inside initpayment" + initMandate);
		logger.info("Inside [PaymentController:callInitMandate] - Inside initpayment" + initMandate);
		long referenceNumber = Instant.now().getEpochSecond();
		try {
			initMandate.setReferenceNumber(referenceNumber);
			saveToDB.saveRequestToDB(initMandate);
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException consEx = (ConstraintViolationException) e.getCause();
				String constraintName = consEx.getConstraintName();
				if (constraintName.contains("referenceNumber")) {
					referenceNumber = Instant.now().getEpochSecond();
					initMandate.setReferenceNumber(referenceNumber);
					saveToDB.saveRequestToDB(initMandate);
				}
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail(e.getCause().toString(), dbconfig);
			} else {
				log.error(e.getLocalizedMessage());
				logger.severe("Inside [PaymentController:callInitMandate] - Exception " + e.getLocalizedMessage());
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail(e.getLocalizedMessage(), dbconfig);
			}
		}
		InitResponseOutput initResponse = new InitResponseOutput();
		try {
			if (initMandate.getEmail() == null || initMandate.getMobileNo() == null
					|| initMandate.getNameOnCard() == null || initMandate.getIdValue() == null
					|| initMandate.getClientType() == 0) {
				log.info("Mandatory value is empty! ");
				logger.info("Inside [PaymentController:callInitMandate] - Mandatory value is empty! ");
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail("Inside [PaymentController:callInitMandate] - Mandatory value is empty! ",
						dbconfig);
				initResponse.setResponseCode("01");
				initResponse.setErrorMsg("Mandatory field not found ");
			} else {
				initResponse = curlecPaymentService.callCurlecNewMandateService(initMandate);
				log.info("Response from Curlec service " + initResponse);
				logger.info(
						"Inside [PaymentController:callInitMandate] - Response from Curlec service " + initResponse);
			}
			// To add logs in DB
			PaymentLogs paymentLogs = new PaymentLogs();
			paymentLogs.setRequest(initMandate.toString());
			paymentLogs.setResponse(initResponse.toString());
			saveToDB.saveRequestToDB(paymentLogs);
		} catch (InternalServerError e) {
			log.error("Exception in init payment" + e);
			logger.info("Inside [PaymentController:callInitMandate] - Exception in init payment" + e);
			initResponse.setResponseCode("01");
			initResponse.setErrorMsg("Internal Server Error");
			EmailUtility emailUtility = new EmailUtility();
			emailUtility.sentEmail(initResponse.toString(), dbconfig);
		} catch (HttpServerErrorException e) {
			log.error("Exception in init payment" + e);
			logger.info("Inside [PaymentController:callInitMandate] - Exception in init payment" + e);
			initResponse.setResponseCode("01");
			initResponse.setErrorMsg("Curlec  Server not reachable");
			EmailUtility emailUtility = new EmailUtility();
			emailUtility.sentEmail(initResponse.toString(), dbconfig);
		}
		return initResponse;
	}

	// Curlec
	@ResponseBody
	@PostMapping(value = "/charge")
	public Object generateCurlecUrl(@RequestBody ChargeUserRequest chargeUserRequest) throws Exception {
		log.info("Inside charge ");
		logger.info("Inside [PaymentController:generateCurlecUrl] - Inside charge");
		ChargeUserResponse paymentResponseDB = new ChargeUserResponse();
		ChargeUserResponseOutput paymentResponse = new ChargeUserResponseOutput();

		/*
		 * chargeUserRequest.setAmount(amount);
		 * 
		 * chargeUserRequest.setRedirectUrl(redirectUrl);
		 * chargeUserRequest.setRef1(refNumber);
		 * chargeUserRequest.setBillCode(billCode);
		 * chargeUserRequest.setWithOTP(withOTP);
		 */

		log.info("chargeUserRequest " + chargeUserRequest);
		logger.info("Inside [PaymentController:generateCurlecUrl] - chargeUserRequest " + chargeUserRequest);
//		merchantCallbackUrl = )chargeUserRequest.getCallBackUrl();
//		paymentCallBackUrl = dbvalues.get(GlobalConstants.PAYMENT_CALLBACK_URL);
		PaymentProcessorsysconfig callbackUrlsfromRedis = getSysConfigvalue(GlobalConstants.PAYMENT_CALLBACK_URL);
		paymentCallBackUrl = callbackUrlsfromRedis.getValue();
		System.out.println("paymentCallBackUrl **************************************" + paymentCallBackUrl);
		log.debug("merchantCallbackUrl " + chargeUserRequest.getCallBackUrl());
		logger.info("Inside [PaymentController:generateCurlecUrl] - merchantCallbackUrl "
				+ chargeUserRequest.getCallBackUrl());
//		chargeUserRequest.setCallBackUrl(paymentCallBackUrl);
		log.debug("chargeUserRequest after setting payment callback url " + chargeUserRequest);
		logger.info(
				"Inside [PaymentController:generateCurlecUrl] - ChargeUserRequest after setting payment callbackurl "
						+ chargeUserRequest);
		log.info("WithOtp " + chargeUserRequest.getWithOtp());
		logger.info("Inside [PaymentController:generateCurlecUrl] - WithOtp " + chargeUserRequest.getWithOtp());
		PaymentProcessorsysconfig redisCallBackUrl = getSysConfigvalue(GlobalConstants.PAYMENT_CALLBACK_URL);
		paymentCallBackUrl = redisCallBackUrl.getValue();
		PaymentProcessorsysconfig redisSimulator = getSysConfigvalue(GlobalConstants.SIMULATOR_CALL);
		simulator = redisSimulator.getValue();
		boolean withotp = chargeUserRequest.getWithOtp();
		if (simulator.equals("true")) {
			try {
				if (withotp) {
					saveToDB.saveRequestToDB(chargeUserRequest);
//					chargeUserRequest.setCallBackUrl(paymentCallBackUrl);
					String responseStr = simulatorChargeUrl(chargeUserRequest, paymentCallBackUrl);
					paymentResponse.setResponseCode("00");
					paymentResponse.setChargeNowWithOtpUrl(responseStr);
					paymentResponse.setInvoiceNumber(
							chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
					paymentResponse.setBillCode(chargeUserRequest.getBillCode());
					paymentResponse.setRefNumber(chargeUserRequest.getRefNumber());

					paymentResponseDB.setResponseCode("00");
					paymentResponseDB.setChargeNowWithOtpUrl(responseStr);
					paymentResponseDB.setInvoiceNumber(
							chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
					paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
					paymentResponseDB.setRefNumber(chargeUserRequest.getRefNumber());
					saveToDB.saveResponseToDB(paymentResponseDB);
				} else if (withotp == false) {
					String ccTransaction = initMandateResponseEntityRepository
							.findByRefNo(chargeUserRequest.getRefNumber());
					log.info("ccTransaction from db" + ccTransaction);
					long unixTime = System.currentTimeMillis();
					log.info("unixTime" + unixTime);
					ccTransaction = ccTransaction + "*" + unixTime;
					log.info("ccTransaction " + ccTransaction);
					logger.info("Inside [PaymentController:generateCurlecUrl] - ccTransaction " + ccTransaction);
					paymentResponse.setResponseCode("00");
					paymentResponse.setCollection_status("SUCCESSFULLY_COMPLETE");
					paymentResponse.setBillCode(chargeUserRequest.getBillCode());
					paymentResponse.setInvoiceNumber(
							chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
					paymentResponse.setCcTransactionId(ccTransaction);
					paymentResponse.setRefNumber(chargeUserRequest.getRefNumber());
					paymentResponseDB.setResponseCode("00");
					paymentResponseDB.setMethod(CurlecMethod.CURLEC_CALLBACK_WITHOUTOTP.toString());
					paymentResponseDB.setCollection_status("SUCCESSFULLY_COMPLETE");
					paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
					paymentResponseDB.setInvoiceNumber(
							chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
					paymentResponseDB.setCcTransactionId(ccTransaction);
					paymentResponseDB.setRefNumber(chargeUserRequest.getRefNumber());
					saveToDB.saveResponseToDB(paymentResponseDB);

				}
			} catch (Exception e) {
				log.info("Exception " + e);
				logger.info("Inside [PaymentController:generateCurlecUrl] - Exception " + e);
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail(e.getCause().toString(), dbconfig);
			}
		} else if (simulator.equals("false")) {
			try {
				if (withotp) {
					saveToDB.saveRequestToDB(chargeUserRequest);
//					chargeUserRequest.setCallBackUrl(paymentCallBackUrl);
					String curlecRequestUrl = curlecPaymentService.callChargeWithOtpUrl(chargeUserRequest,
							paymentCallBackUrl);
					log.info("ChargeWithOtpResponse url to hit curlec" + curlecRequestUrl);
					logger.info("Inside [PaymentController:generateCurlecUrl] - ChargeWithOtpResponse url to hit curlec"
							+ curlecRequestUrl);
					if (StringUtils.isNotBlank(curlecRequestUrl)) {

						paymentResponse.setChargeNowWithOtpUrl(curlecRequestUrl);
						paymentResponse.setInvoiceNumber(
								chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
						paymentResponse.setBillCode(chargeUserRequest.getBillCode());
						paymentResponse.setRefNumber(chargeUserRequest.getRefNumber());
						// truncate since it is long
						String curlecUrl = curlecRequestUrl.substring(33);
						paymentResponseDB.setChargeNowWithOtpUrl(curlecRequestUrl);
						paymentResponseDB.setInvoiceNumber(
								chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo());
						paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
						paymentResponseDB.setRefNumber(chargeUserRequest.getRefNumber());
					} else {
						paymentResponse.setErrorMsg("FAILURE");
					}
				} else if (withotp == false) {
					// chargeUserRequest.setCallBackUrl(callBackUrl);
					saveToDB.saveRequestToDB(chargeUserRequest);
					ResponseEntity<String> chargeWithOtp = curlecPaymentService.callChargeNow(chargeUserRequest);
					log.info("ChargeWithResponse url to hit curlec" + chargeWithOtp);
					logger.info("Inside [PaymentController:generateCurlecUrl] - ChargeWithResponse url to hit curlec"
							+ chargeWithOtp);
					JSONObject bodyJson = new JSONObject(chargeWithOtp.getBody().toString());
					log.info("bodyJson " + bodyJson);
					JSONObject responseJson = null;
					if (bodyJson.getJSONArray("Status").get(0).toString().equals("200")) {
						log.info("Status code in response is  " + bodyJson.getJSONArray("Status").get(0).toString());
						log.info("Response " + bodyJson.getJSONArray("Response").get(0).toString());
						responseJson = new JSONObject(bodyJson.getJSONArray("Response").get(0).toString());
						log.info("responseJson " + responseJson);
						logger.info("Inside [PaymentController:generateCurlecUrl] - responseJson " + responseJson);
						log.info("chargeWithOtp " + chargeWithOtp);
						log.info("Status code " + chargeWithOtp.getStatusCode().toString());

						if (chargeWithOtp != null && chargeWithOtp.getStatusCode().toString().contains("200 OK")
								&& bodyJson.getJSONArray("Status").get(0).toString().equals("200")) {
							log.info("Inside Condition Success ");
							paymentResponse.setResponseCode("200");
							paymentResponseDB.setResponseCode("200");

							if (responseJson.get("collection_status") != null
									&& responseJson.getJSONArray("collection_status").get(0).toString() != null) {
								paymentResponse.setCollection_status(
										responseJson.getJSONArray("collection_status").get(0).toString());
								paymentResponseDB.setCollection_status(
										responseJson.getJSONArray("collection_status").get(0).toString());
							}
							if (responseJson.get("invoice_number") != null
									&& responseJson.getJSONArray("invoice_number").get(0).toString() != null) {
								paymentResponse.setBillCode(chargeUserRequest.getBillCode());
								paymentResponseDB.setBillCode(chargeUserRequest.getBillCode());
								paymentResponse.setInvoiceNumber(
										responseJson.getJSONArray("invoice_number").get(0).toString());
								paymentResponseDB.setInvoiceNumber(
										responseJson.getJSONArray("invoice_number").get(0).toString());
							}
							if (responseJson.get("cc_transaction_id") != null
									&& responseJson.getJSONArray("cc_transaction_id").get(0).toString() != null) {
								paymentResponse.setCcTransactionId(
										responseJson.getJSONArray("cc_transaction_id").get(0).toString());
								paymentResponseDB.setCcTransactionId(
										responseJson.getJSONArray("cc_transaction_id").get(0).toString());
							}
							if (responseJson.get("reference_number") != null
									&& responseJson.getJSONArray("reference_number").get(0).toString() != null) {
								paymentResponse
										.setRefNumber(responseJson.getJSONArray("reference_number").get(0).toString());
								paymentResponseDB
										.setRefNumber(responseJson.getJSONArray("reference_number").get(0).toString());
							}
							paymentResponseDB.setMethod(CurlecMethod.CURLEC_CALLBACK_WITHOUTOTP.toString());
							paymentResponseDB.setResponseMessage(responseJson.toString());
						}
					} else if (chargeWithOtp != null && chargeWithOtp.getStatusCode().toString().contains("200")
							&& !bodyJson.getJSONArray("Status").get(0).toString().equals("200")) {
						log.info("Status code in else loop is  " + bodyJson.getJSONArray("Status").get(0).toString());
						if (!bodyJson.getJSONArray("Message").get(0).toString().isEmpty()
								&& !bodyJson.getJSONArray("Message").get(0).toString().isBlank()) {
							paymentResponse.setErrorMsg(bodyJson.getJSONArray("Message").get(0).toString());
							paymentResponse.setResponseCode("01");
						}

						else {
							paymentResponse.setErrorMsg("FAILURE");
							paymentResponse.setResponseCode("01");
							paymentResponseDB.setResponseCode("01");
							EmailUtility emailUtility = new EmailUtility();
							emailUtility.sentEmail(paymentResponse.toString(), dbconfig);
						}
					} else if (chargeWithOtp == null) {
						paymentResponse.setErrorMsg("FAILURE");
						paymentResponse.setResponseCode("01");
						paymentResponseDB.setResponseCode("01");
						EmailUtility emailUtility = new EmailUtility();
						emailUtility.sentEmail(paymentResponse.toString(), dbconfig);
					}
				}
				if (chargeUserRequest.getCallBackUrl() != null) {
					log.info("Callback is not null");
					logger.info("Inside [PaymentController:generateCurlecUrl] -Callback is not null");
				}

				saveToDB.saveResponseToDB(paymentResponseDB);
			} catch (Exception e) {
				paymentResponse.setErrorMsg(e.getLocalizedMessage());
				paymentResponse.setResponseCode("01");
			}
		}

		// To add logs in DB
		PaymentLogs paymentLogs = new PaymentLogs();
		paymentLogs.setRequest(chargeUserRequest.toString());
		paymentLogs.setResponse(paymentResponse.toString());
		saveToDB.saveRequestToDB(paymentLogs);

		return paymentResponse;
	}

	private PaymentProcessorsysconfig getSysConfigvalue(String key) {
		PaymentProcessorsysconfig readValuesFromRedis = dbconfig.readValuesFromRedis(key);
		return readValuesFromRedis;
	}

	// Curlec
	@ResponseBody
	@PostMapping(value = "/checkcollectionstatus")
	public Object checkCollectionStatus(@RequestBody CollectionStatusRequest collectionStatusRequest) throws Exception {
		log.info("Inside collectionStatusRequest " + collectionStatusRequest);
		CollectionStatusResponseOutput statusResponse = new CollectionStatusResponseOutput();
		CollectionStatusResponse statusResponsedb = new CollectionStatusResponse();
		saveToDB.saveRequestToDB(collectionStatusRequest);
		ResponseEntity<String> response = null;
		ObjectMapper mapper = new ObjectMapper();
		String res;
		log.info("Inside checkcollectionstatus " + collectionStatusRequest);
		logger.info("Inside [PaymentController:checkCollectionStatus] - Inside checkcollectionstatus "
				+ collectionStatusRequest);
		PaymentProcessorsysconfig redisSimulator = getSysConfigvalue(GlobalConstants.SIMULATOR_CALL);
		simulator = redisSimulator.getValue();
		if (simulator.equals("true")) {
			log.info("Inside Simulator ");
			statusResponse.setCollection_status("SUCCESSFULLY_COMPLETE");
			statusResponse.setCc_transaction_id(collectionStatusRequest.getCcTransactionId());
			statusResponse.setResponseCode("00");

			statusResponsedb.setCollection_status("SUCCESSFULLY_COMPLETE");
			statusResponsedb.setCc_transaction_id(collectionStatusRequest.getCcTransactionId());
			statusResponsedb.setResponseCode("00");
			saveToDB.saveResponseToDB(statusResponsedb);
		} else if (simulator.equals("false")) {
			log.info("Inside normal flow simulator - false ");
			logger.info("Inside [PaymentController:checkCollectionStatus] - Inside normal flow simulator - false ");
			try {
				if (collectionStatusRequest.getCcTransactionId() == null
						|| collectionStatusRequest.getClientType() == null) {
					log.info("Mandatory value is empty..!! ");
					logger.info("Inside [PaymentController:checkCollectionStatus] - Mandatory value is empty..!! ");
					statusResponse.setResponseCode("01");
					statusResponse.setErrorMsg("Mandatory value is empty");
					EmailUtility emailUtility = new EmailUtility();
					emailUtility.sentEmail(statusResponse.toString(), dbconfig);
				} else {
					ResponseEntity<String> curlecStatusResponse = curlecPaymentService
							.checkCurlecStatus(collectionStatusRequest);
					statusResponse.setResponseCode("00");
					statusResponsedb.setResponseCode("00");
					log.info("Response from curlec collection status " + statusResponse);
					logger.info(
							"Inside [PaymentController:checkCollectionStatus] - Response from curlec collection status "
									+ statusResponse);
					if (curlecStatusResponse != null && curlecStatusResponse.getStatusCodeValue() == 200) {
						JSONObject responseJson = new JSONObject(curlecStatusResponse.getBody().toString());
						log.info("responseJson " + responseJson);

						if (responseJson.getJSONArray("collection_status").get(0).toString() != null) {
							statusResponse.setCollection_status(
									responseJson.getJSONArray("collection_status").get(0).toString());
							statusResponsedb.setCollection_status(
									responseJson.getJSONArray("collection_status").get(0).toString());
						}
						if (responseJson.getJSONArray("cc_transaction_id").get(0).toString() != null) {
							statusResponse.setCc_transaction_id(
									responseJson.getJSONArray("cc_transaction_id").get(0).toString());
							statusResponsedb.setCc_transaction_id(
									responseJson.getJSONArray("cc_transaction_id").get(0).toString());
						}
						/*
						 * if(responseJson.getJSONArray("reference_number").get(0).toString() != null) {
						 * statusResponse.setCc_transaction_id(responseJson.getJSONArray(
						 * "reference_number").get(0).toString());
						 * statusResponsedb.setCc_transaction_id(responseJson.getJSONArray(
						 * "reference_number").get(0).toString()); }
						 */
					}

				}
				saveToDB.saveResponseToDB(statusResponsedb);
			} catch (Exception e) {
				log.error("Exception in init status" + e);
				logger.info("Inside [PaymentController:checkCollectionStatus] - Exception in init status" + e);
				statusResponse.setResponseCode("01");
				statusResponse.setErrorMsg(e.getLocalizedMessage());
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail(statusResponse.toString(), dbconfig);
			}
		}
		// To add logs in DB
		PaymentLogs paymentLogs = new PaymentLogs();
		paymentLogs.setRequest(collectionStatusRequest.toString());
		paymentLogs.setResponse(statusResponse.toString());
		saveToDB.saveRequestToDB(paymentLogs);
		return statusResponse;
	}

	@ResponseBody
	@RequestMapping(value = "mobicallback", method = { RequestMethod.GET, RequestMethod.POST })
	public void processCallbackTest(@RequestParam("input") String input)
	// @RequestParam("fpx_notes") String fpx_notes)
	{

		System.out.println("Callback:" + input);
		log.info("Callback : +input");
	}

	/*
	 * @ResponseBody
	 * 
	 * @PostMapping("/api/payment/callback/new-mandate") //@RequestMapping(value =
	 * "//api/payment/callback/new-mandate", method =
	 * {RequestMethod.GET,RequestMethod.POST}) public CallBackDto
	 * processCallback(@RequestParam("curlec_method") String
	 * curlec_method, @RequestParam("fpx_fpxTxnId") String fpx_fpxTxnId,
	 * 
	 * @RequestParam("fpx_sellerExOrderNo") String
	 * fpx_sellerExOrderNo, @RequestParam("fpx_fpxTxnTime") String fpx_fpxTxnTime,
	 * 
	 * @RequestParam("fpx_sellerOrderNo") String
	 * fpx_sellerOrderNo, @RequestParam("fpx_sellerId") String fpx_sellerId,
	 * 
	 * @RequestParam("fpx_txnCurrency") String
	 * fpx_txnCurrency, @RequestParam("fpx_txnAmount") String fpx_txnAmount,
	 * 
	 * @RequestParam("fpx_buyerName") String
	 * fpx_buyerName, @RequestParam("fpx_buyerBankId") String fpx_buyerBankId,
	 * 
	 * @RequestParam("fpx_debitAuthCode") String
	 * fpx_debitAuthCode, @RequestParam("fpx_type") String fpx_type)
	 * // @RequestParam("fpx_notes") String fpx_notes) { CallBackDto callBackDto =
	 * CallBackDto.builder() .curlec_method(curlec_method)
	 * .fpx_buyerName(fpx_buyerName) .fpx_buyerBankId(fpx_buyerBankId)
	 * .fpx_debitAuthCode(fpx_debitAuthCode) // .fpx_notes(fpx_notes)
	 * .fpx_sellerId(fpx_sellerId) .fpx_sellerOrderNo(fpx_sellerOrderNo)
	 * .fpx_txnAmount(fpx_txnAmount) .fpx_txnCurrency(fpx_txnCurrency)
	 * .fpx_type(fpx_type) .fpx_fpxTxnId(fpx_fpxTxnId)
	 * .fpx_fpxTxnTime(fpx_fpxTxnTime) .fpx_sellerExOrderNo(fpx_sellerExOrderNo)
	 * .build();
	 * 
	 * System.out.println("Callback data:" + callBackDto.toString());
	 * 
	 * CallBackDto res = callBackDtoEntityRepository.save(callBackDto); return res;
	 * }
	 * 
	 * @ResponseBody
	 * 
	 * @PostMapping("/callback/mobypayCallback") public String
	 * MobyprocessCallback(@RequestBody MobiCallBackDto callBackDto) throws
	 * ParseException {
	 * 
	 * Map<String,String> mobipaymentResponse=new LinkedHashMap<String,String>();
	 * SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMMM-yyyy");
	 * SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd"); String
	 * datevalue=callBackDto.getDate(); Date date = dateFormat.parse(datevalue);
	 * 
	 * System.out.println(sdfOut.format(date));
	 * 
	 * String datewise=sdfOut.format(date); MobiCallBackDto mobicallBackDto =
	 * MobiCallBackDto.builder().Aid(callBackDto.getAid()).Amount(callBackDto.
	 * getAmount()).ApproveCode( callBackDto.getApproveCode())
	 * .CardHolderName(callBackDto.getCardHolderName()).CardNo(callBackDto.getCardNo
	 * ()).Date(datewise).Mid(callBackDto.getMid()).OrderId(
	 * callBackDto.getOrderId())
	 * .ResponseCode(callBackDto.getResponseCode()).ResponseDescription(callBackDto.
	 * getResponseDescription()). ResponseMessage(callBackDto.getResponseMessage())
	 * .Tid(callBackDto.getTid()).Uid(callBackDto.getUid()).WalletId(callBackDto.
	 * getWalletId()).Rrn(callBackDto.getRrn()).Time(callBackDto.getTime())
	 * .CardType(callBackDto.getCardType()).CardBrand(callBackDto.getCardBrand()).
	 * Currency(callBackDto.getCurrency()).CountryCode(callBackDto.getCountryCode())
	 * .build();
	 * 
	 * 
	 * 
	 * System.out.println("Mobi Callback data:" +mobicallBackDto);
	 * 
	 * MobiCallBackDto res = mobiCallBackDtoEntityRepository.save(mobicallBackDto);
	 * if(callBackDto.getResponseMessage().contains("Success")) { if(
	 * callBackDto.getOrderId()!=null ) { PaymentRequest paymentRequest=new
	 * PaymentRequest(); for(int i=0;i<getuniqueId.size();i++) { String
	 * uniquevalue=getuniqueId.get(i);
	 * paymentRequest=paymentrequestentityrepository.findbyorderId(uniquevalue);
	 * String merchantcallback=paymentRequest.getCallBackUrl(); String
	 * merchantId=paymentRequest.getMerchantId();
	 * paymentRequest.setCarddetails(callBackDto.getCardNo());
	 * mobipaymentResponse.put("mid", merchantId);
	 * mobipaymentResponse.put("datetime",
	 * callBackDto.getDate()+" "+callBackDto.getTime());
	 * mobipaymentResponse.put("cardNo", callBackDto.getCardNo());
	 * mobipaymentResponse.put("cardHolderName",callBackDto.getCardHolderName());
	 * mobipaymentResponse.put("amount", callBackDto.getAmount());
	 * mobipaymentResponse.put("responseDescription",
	 * "Payment Approved - Card added successfully");
	 * mobipaymentResponse.put("billCode", paymentRequest.getBillCode());
	 * mobipaymentResponse.put("responseMessage", "Success");
	 * mobipaymentResponse.put("tokenId", callBackDto.getWalletId());
	 * mobipaymentResponse.put("cardBrand", callBackDto.getCardBrand());
	 * mobipaymentResponse.put("cardType", callBackDto.getCardType()); String
	 * url=merchantcallback;
	 * 
	 * try { URI uri = new URI(url); result = restTemplate.postForEntity(uri,
	 * mobipaymentResponse, String.class); System.out.println(result.getBody());
	 * return result.getBody(); } catch (URISyntaxException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }else { System.out.println("OrderId does not Exist");
	 * mobipaymentResponse.put("responseDescription", "Incorrect Input");
	 * mobipaymentResponse.put("responseCode", "02");
	 * mobipaymentResponse.put("responseMessage", "Bad Request");
	 * 
	 * } }else { System.out.println("Not Sucess");
	 * mobipaymentResponse.put("responseDescription", "Payment Transaction Failed");
	 * mobipaymentResponse.put("responseCode", "01");
	 * mobipaymentResponse.put("responseMessage", "Insufficient Funds"); } return
	 * result.getBody(); }
	 */
	public String simulatorChargeUrl(ChargeUserRequest chargeUserRequest, String callBackUrl) {
		PaymentProcessorsysconfig serverNameFromRedis = getSysConfigvalue(GlobalConstants.SERVER_NAME);
		String serverName = serverNameFromRedis.getValue();
		PaymentProcessorsysconfig apmerchantId = getSysConfigvalue(GlobalConstants.AP_CURLEC_MERCHANT_ID);
		PaymentProcessorsysconfig apEmpID = getSysConfigvalue(GlobalConstants.AP_CURLEC_EMP_ID);

		PaymentProcessorsysconfig mpMerchantID = getSysConfigvalue(GlobalConstants.MP_CURLEC_MERCHANT_ID);
		PaymentProcessorsysconfig mpEmpID = getSysConfigvalue(GlobalConstants.MP_CURLEC_EMP_ID);

		PaymentProcessorsysconfig mpLegacyEmpfor99 = getSysConfigvalue(GlobalConstants.AP_CURLEC_EMP_ID);
		PaymentProcessorsysconfig mpLegacyMercFor99 = getSysConfigvalue(GlobalConstants.MP_CURLEC_MERCHANT_ID);
		log.info("Inside simulatorChargeUrl ");
		logger.info("Inside [PaymentController:simulatorChargeUrl] - Inside simulatorChargeUrl ");
		String url = "";
		if (chargeUserRequest.getClientType() == 1) {
			url = serverName + "chargeNow?merchantId=" + apmerchantId.getValue() + "&employeeId=" + apEmpID.getValue()
					+ "&refNumber=" + chargeUserRequest.getRefNumber() + "&collectionAmount="
					+ chargeUserRequest.getAmount() + "&invoiceNumber=" + chargeUserRequest.getBillCode() + "-"
					+ chargeUserRequest.getUniqueRequestNo() + "&collectionCallbackUrl=" + callBackUrl + "&redirectUrl="
					+ chargeUserRequest.getRedirectUrl() + "&method=chargenowOTP";
		} else if (chargeUserRequest.getClientType() == 2) {
			url = serverName + "chargeNow?merchantId=" + mpMerchantID.getValue() + "&employeeId=" + mpEmpID.getValue()
					+ "&refNumber=" + chargeUserRequest.getRefNumber() + "&collectionAmount="
					+ chargeUserRequest.getAmount() + "&invoiceNumber=" + chargeUserRequest.getBillCode() + "-"
					+ chargeUserRequest.getUniqueRequestNo() + "&collectionCallbackUrl=" + callBackUrl + "&redirectUrl="
					+ chargeUserRequest.getRedirectUrl() + "&method=chargenowOTP";
		} else if (chargeUserRequest.getClientType() == 99) {
			url = serverName + "chargeNow?merchantId=" + mpLegacyEmpfor99.getValue() + "&employeeId="
					+ mpLegacyMercFor99.getValue() + "&refNumber=" + chargeUserRequest.getRefNumber()
					+ "&collectionAmount=" + chargeUserRequest.getAmount() + "&invoiceNumber="
					+ chargeUserRequest.getBillCode() + "-" + chargeUserRequest.getUniqueRequestNo()
					+ "&collectionCallbackUrl=" + callBackUrl + "&redirectUrl=" + chargeUserRequest.getRedirectUrl()
					+ "&method=chargenowOTP";
		}
		log.info("URL in callChargeWithOtpUrl " + url);
		logger.info("Inside [PaymentController:simulatorChargeUrl] - URL in callChargeWithOtpUrl " + url);
		return url;
	}

	// @ResponseBody
	@GetMapping(value = "/chargeNow")
	public ResponseEntity<Void> simulateCurlecCharge(@RequestParam String merchantId, @RequestParam String employeeId,
			@RequestParam String refNumber, @RequestParam String collectionAmount, @RequestParam String invoiceNumber,
			@RequestParam String collectionCallbackUrl, @RequestParam String redirectUrl, @RequestParam String method) {
		log.info("Inside simulatorCharge ");
		logger.info("Inside [PaymentController:simulateCurlecCharge] - Inside simulatorCharge ");
		String url = null;
		ResponseEntity<String> responseFromMerchant;
		// curlecPaymentService.simulateChargeCallback(callBackUrl);
		try {
			// getSimulatorCallback("nFk-09052022-126*1",invoiceNumber,"SUCCESSFULLY_COMPLETE",refNumber);
			CurlecCallback curlecCallbackResponse = new CurlecCallback();
			log.info("invoiceNumber " + invoiceNumber);
			String ccTransaction = initMandateResponseEntityRepository.findByRefNo(refNumber);
			long unixTime = System.currentTimeMillis();

			ccTransaction = ccTransaction + "*" + unixTime;
			log.info("ccTransaction " + ccTransaction);
			logger.info("Inside [PaymentController:simulateCurlecCharge] - ccTransaction " + ccTransaction);
			/*
			 * for(int i=0;i<9;i++) { ccTransactionId += i; }
			 */
			curlecCallbackResponse.setCcTransactionId(ccTransaction);
			curlecCallbackResponse.setBillCode(invoiceNumber.split("-")[0]);
			curlecCallbackResponse.setInvoiceNumber(invoiceNumber);
			curlecCallbackResponse.setCollectionStatus("SUCCESSFULLY_COMPLETE");
			curlecCallbackResponse.setRefNumber(refNumber);
			String curlecCallbackUrl = callBackUrl.getCurlecCallbackUrl(curlecCallbackResponse);
			responseFromMerchant = curlecPaymentService.callCurlecCallback(curlecCallbackResponse, curlecCallbackUrl);
			log.info("responseFromMerchant " + responseFromMerchant);
			logger.info(
					"Inside [PaymentController:simulateCurlecCharge] - Response From Merchant " + responseFromMerchant);
			url = redirectUrl + "?reference_number=" + refNumber + "&invoice_number=" + invoiceNumber
					+ "&collection_status=SUCCESSFULLY_COMPLETE&cc_transaction_id=" + ccTransaction;
			log.info("Redirect url " + redirectUrl);
			logger.info("Inside [PaymentController:simulateCurlecCharge] - Redirect url " + redirectUrl);
		} catch (Exception e) {
			log.info("Exception in simulateCurlecCharge " + e);
			logger.info("Inside [PaymentController:simulateCurlecCharge] - Exception in simulateCurlecCharge " + e);
			EmailUtility emailUtility = new EmailUtility();
			emailUtility.sentEmail(e.getLocalizedMessage().toString(), dbconfig);
		}

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}

	@ResponseBody
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirectSimulator(@RequestParam String collection_status, @RequestParam String reference_number,
			@RequestParam String invoice_number, @RequestParam String cc_transaction_id) {
		log.info("Inside redirect");
		// String response = "?reference_number="+refNumber +"&invoice_number="
		// +invoiceNumber
		// +"&collection_status=SUCCESSFULLY_COMPLETE&cc_transaction_id=nFk-09052022-126*1";

		// String url = redirectUrl + "?reference_number="+refNumber +"&invoice_number="
		// +invoiceNumber
		// +"&collection_status=SUCCESSFULLY_COMPLETE&cc_transaction_id=nFk-09052022-126*1";
		// return
		// ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
		return " ";
	}

	@ResponseBody
	@RequestMapping(value = { "/callback" }, method = RequestMethod.POST)
	public CurlecCallback getSimulatorCallback(CurlecCallback curlecCallbackResponse) throws Exception {
		// CurlecCallback curlecCallbackResponse = new CurlecCallback();
		ResponseEntity<String> responseFromMerchant;
		log.info("Inside getSimulatorCallback " + curlecCallbackResponse.getRefNumber() + "and  invoice number "
				+ curlecCallbackResponse.getInvoiceNumber());
		/*
		 * curlecCallbackResponse.setCcTransactionId(ccTransactionId) ;
		 * curlecCallbackResponse.setBillCode(invoiceNumber.toString().split("-")[0]);
		 * curlecCallbackResponse.setInvoiceNumber(invoiceNumber.toString()) ;
		 * curlecCallbackResponse.setCollectionStatus(collectionStatus.toString()) ;
		 * curlecCallbackResponse.setRefNumber(refNumber.toString()) ;
		 */
		// responseFromMerchant =
		// curlecPaymentService.callCurlecCallback(curlecCallbackResponse,merchantCallbackUrl);
		// log.info("responseFromMerchant " +responseFromMerchant);
		return curlecCallbackResponse;
	}

	// Curlec
	@ResponseBody
	@PostMapping(value = "/queryStatus")
	public Object queryCollectionStatus(@RequestBody QueryStatusRequest collectionStatusRequest) throws Exception {
		log.info("Inside collectionStatusRequest " + collectionStatusRequest);
		CollectionStatusResponseOutput statusResponse = new CollectionStatusResponseOutput();
		CollectionStatusResponse statusResponsedb = new CollectionStatusResponse();

		PaymentProcessorsysconfig redisSimulator = getSysConfigvalue(GlobalConstants.SIMULATOR_CALL);
		simulator = redisSimulator.getValue();
		ResponseEntity<CurlecRequeryResponse> curlecStatusResponse = null;
		if (simulator.equals("true")) {
			// testing with hardcoded values
			if (collectionStatusRequest.getStatusCode() == 1) {
				statusResponse.setCollection_status("SUCCESSFULLY_COMPLETE");
			} else if (collectionStatusRequest.getStatusCode() == 2) {
				statusResponse.setCollection_status("Not Exist");
			} else if (collectionStatusRequest.getStatusCode() == 3) {
				statusResponse.setCollection_status("INVALID_TRANSACTION");
			} else if (collectionStatusRequest.getStatusCode() == 4) {
				statusResponse.setCollection_status("TRANSACTION_IN_PROGRESS");
			} else if (collectionStatusRequest.getStatusCode() == 5) {
				statusResponse.setCollection_status("INSUFFICIENT_FUNDS");
			} else if (collectionStatusRequest.getStatusCode() == 6) {
				statusResponse.setCollection_status("EXCEEDS_WITHDRAWAL_LIMIT");
			} else if (collectionStatusRequest.getStatusCode() == 7) {
				statusResponse.setCollection_status("FUNCTION_NOT_PERMITTED");
			} else if (collectionStatusRequest.getStatusCode() == 8) {
				statusResponse.setCollection_status("PICK_UP_CARD");
			} else if (collectionStatusRequest.getStatusCode() == 9) {
				statusResponse.setCollection_status("DO_NOT_HONOUR");
			} else if (collectionStatusRequest.getStatusCode() == 10) {
				statusResponse.setCollection_status("RESUBMIT_WITH_ALTERNATIVE");
			} else if (collectionStatusRequest.getStatusCode() == 11) {
				statusResponse.setCollection_status("CVV_VALIDATION_ERROR");
			}
		} else if (simulator.equals("false")) {
			// Check if transaction is in progress in Service DB
			List<ChargeUserResponse> collectionStatus = chargeUserResponseRepo
					.findCollectionStatusbyBillCode(collectionStatusRequest.getBillCode());

			ChargeUserResponse validateCollectionStatus = validateCollectionStatus(collectionStatus);

			if (collectionStatus != null && !collectionStatus.isEmpty()) {
				RequeryRequest requeryreq = new RequeryRequest();
				requeryreq.setInvoiceNumber(collectionStatus.get(0).getInvoiceNumber());
				List<ChargeUserResponse> collectionDB = null;
				collectionDB = chargeUserResponseRepo
						.findCollectionStatusbyBillCode(collectionStatusRequest.getBillCode());
				curlecStatusResponse = curlecPaymentService.curlecCollectionStatus(requeryreq);
				String status = null;
				if(curlecStatusResponse.getBody().toString().contains("409")) {
					status= "409";
				}else if(curlecStatusResponse.getBody().toString().contains("201")){
					status ="201";
				}
				
				
				statusResponse.setResponseCode(status);
				statusResponsedb.setResponseCode(status);
				statusResponse.setResponse(curlecStatusResponse.getBody().toString());
				log.info("Response from curlec collection status " + statusResponse);
				logger.info("Inside [PaymentController:checkCollectionStatus] - Response from curlec collection status "
						+ statusResponse);
				if (curlecStatusResponse != null
						&& curlecStatusResponse.getBody().getStatus().toString().contains("201")
						&& curlecStatusResponse.getBody().getResponse() != null
						&& !curlecStatusResponse.getBody().getResponse().isEmpty()
						&& !curlecStatusResponse.getBody().getResponse().get(0).isEmpty()) {
					if (collectionStatus.get(0).getCcTransactionId() == null) {
						collectionStatus.get(0).setCcTransactionId(
								curlecStatusResponse.getBody().getResponse().get(0).get(0).getCcTransactionId().get(0));
					}
					collectionStatus.get(0).setCollection_status(
							curlecStatusResponse.getBody().getResponse().get(0).get(0).getCollectionStatus().get(0));
					collectionStatus.get(0).setUpdatedAt(new Date(System.currentTimeMillis()));
				}
			}else if(collectionStatus.isEmpty()) {
				statusResponse.setResponseCode("404");
				statusResponse.setErrorMsg("Record not found in internal application(Credit charge user Response Table");
			}else {
				ChargeUserResponse response = new ChargeUserResponse();
				boolean recordFound = false;
				for (ChargeUserResponse chargeUserResponse : collectionStatus) {
					if (chargeUserResponse.getCollection_status() != null && chargeUserResponse.getCollection_status()
							.equalsIgnoreCase(GlobalConstants.SUCCESSFULLY_COMPLETE)) {
						recordFound = Boolean.TRUE;
						response = collectionStatus.stream()
								.filter(o -> o.getCollection_status().equals(GlobalConstants.SUCCESSFULLY_COMPLETE))
								.findAny().orElse(null);
						statusResponse.setCc_transaction_id(response.getCcTransactionId());
						statusResponse.setCollection_status(response.getCollection_status());
						statusResponse.setReference_number(response.getRefNumber());
						statusResponse.setResponseCode("200");
					}
				}
				if (!collectionStatus.isEmpty() && collectionStatus != null && !recordFound) {
					statusResponse.setReference_number(collectionStatus.get(0).getRefNumber());
					statusResponse.setCc_transaction_id(collectionStatus.get(0).getCcTransactionId());
					statusResponse.setCollection_status(collectionStatus.get(0).getCollection_status());
					statusResponse.setResponseCode("404");
					statusResponse.setErrorMsg("TransactionId and Collection status Null");
				}
			}
		}

		PaymentLogs paymentLogs = new PaymentLogs();
		paymentLogs.setRequest(collectionStatusRequest.toString());
		paymentLogs.setResponse(statusResponse.toString());
		saveToDB.saveRequestToDB(paymentLogs);
		return curlecStatusResponse;

	}

	private ChargeUserResponse validateCollectionStatus(List<ChargeUserResponse> collectionStatus) {
		ChargeUserResponse resultObject = null;
		boolean tranInProgressPresent = Boolean.FALSE;
		boolean tranSuccessPresent = Boolean.FALSE;
		for (ChargeUserResponse chargeUserResponse : collectionStatus) {
			if (chargeUserResponse.getCollection_status() != null && chargeUserResponse.getCollection_status()
					.equalsIgnoreCase(GlobalConstants.TRANSACTION_IN_PROGRESS)) {
				tranInProgressPresent = Boolean.TRUE;
			}
			if (chargeUserResponse.getCollection_status() != null && chargeUserResponse.getCollection_status()
					.equalsIgnoreCase(GlobalConstants.SUCCESSFULLY_COMPLETE)) {
				tranInProgressPresent = Boolean.TRUE;
			}
		}

		if (tranInProgressPresent && tranSuccessPresent) {
			Comparator<ChargeUserResponse> recentdatedObject = Comparator.comparing(ChargeUserResponse::getCreatedAt);
			resultObject = collectionStatus.stream().max(recentdatedObject).get();
			if (resultObject != null
					&& resultObject.getCollection_status().equalsIgnoreCase(GlobalConstants.TRANSACTION_IN_PROGRESS)) {
				EmailUtility emailUtility = new EmailUtility();
				emailUtility.sentEmail("TRANSACTION_IN_PROGRESS is dated after SUCCESSFULLY_COMPLETE", dbconfig);
			}
		} else if (tranInProgressPresent) {
			Comparator<ChargeUserResponse> recentdatedObject = Comparator.comparing(ChargeUserResponse::getCreatedAt);
			resultObject = collectionStatus.stream().max(recentdatedObject).get();
		}
		return resultObject;
	}

	
	@ResponseBody
	@RequestMapping(value = {
			"/ssVoid" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCurlecVoid(@RequestParam Object ccTransactionId, @RequestParam Object merchantId,
			@RequestParam Object invoiceNumber, @RequestParam Object employeeId, @RequestParam Object reason) throws Exception {
		CurlecVoid curlecVoid = new CurlecVoid();
		CollectionStatusResponseOutput statusResponse = new CollectionStatusResponseOutput();
		ResponseEntity<String> responseFromMerchant;
		CollectionStatusResponse statusResponsedb = new CollectionStatusResponse();
		log.info("Inside getCurlecVoid" );
		HttpStatus status = HttpStatus.OK;
		curlecVoid.setCcTransactionId(ccTransactionId.toString());
		curlecVoid.setMerchantId(merchantId.toString());
		curlecVoid.setInvoiceNumber(invoiceNumber.toString());
		curlecVoid.setEmployeeId(employeeId.toString());
		curlecVoid.setReason(reason.toString());
		ResponseEntity<String> curlecCallbackUrl = callBackUrl.curlecVoid(curlecVoid);
		
		if (curlecCallbackUrl != null) {
			JSONObject responseJson = new JSONObject(curlecCallbackUrl.getBody().toString());
			log.info("responseJson " + responseJson);
			if (responseJson.getJSONArray("Response").get(0).toString() != null) {
				statusResponsedb.setResponseMessage(responseJson.getJSONArray("Response").get(0).toString());
			}
			if (responseJson.getJSONArray("Status").get(0).toString() != null) {
				statusResponsedb.setResponseCode(responseJson.getJSONArray("Status").get(0).toString());
			}
			statusResponsedb.setCc_transaction_id(ccTransactionId.toString());
			

	}
	saveToDB.saveResponseToDB(statusResponsedb);
	return curlecCallbackUrl;
	}
	

	@GetMapping(value = "/ping")
	public String pingServer() {
		return "Server is up";
	}

}
