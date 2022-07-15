package com.mobpay.Payment.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.mobpay.Payment.DbConfig;
import com.mobpay.Payment.ReadProperties;
import com.mobpay.Payment.Repository.ChargeUserRequestEntityRepository;
import com.mobpay.Payment.Repository.CollectionStatusRequest;
import com.mobpay.Payment.Repository.MobyversaMandateRequestDtoEntityRepository;
import com.mobpay.Payment.Repository.NewMandateRequestDtoEntityRepository;
import com.mobpay.Payment.Repository.PaymentProcessorConfigRepository;
import com.mobpay.Payment.Repository.ReferenceNumberRepo;
import com.mobpay.Payment.Repository.SaveToDB;
import com.mobpay.Payment.dao.ChargeNowEntity;
import com.mobpay.Payment.dao.ChargeUserRequest;
import com.mobpay.Payment.dao.ChargeUserResponseOutput;
import com.mobpay.Payment.dao.CurlecCallback;
import com.mobpay.Payment.dao.Curlec_MandateResponse;
import com.mobpay.Payment.dao.InitMandate;
import com.mobpay.Payment.dao.InitPayment;
import com.mobpay.Payment.dao.InitPaymentRepository;
import com.mobpay.Payment.dao.InitResponseOutput;
import com.mobpay.Payment.dao.MandateStatusRequest;
import com.mobpay.Payment.dao.MobyversaNewMandateRequestDto;
import com.mobpay.Payment.dao.NewMandateRequestDto;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetInstancesFromServiceLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.ReferenceNumber;
import com.mobpay.Payment.dao.VGSInput;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class CurlecPaymentServiceImpl implements CurlecPaymentService {

	@Autowired
	DbConfig dbconfig;

	@Autowired
	SaveToDB saveToDB;

	protected String curlecUrl;

	@Autowired
	private ReferenceNumberRepo referenceNumberRepo;

	@Autowired
	private ChargeUserRequestEntityRepository chargeUserRequestEntityRepository;

	static RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> result = null;

	public String callChargeWithOtpUrl(ChargeUserRequest paymentRequest, String callBackUrl) {
		DecimalFormat df = new DecimalFormat("0.00");
		String url = "";
		log.info("Inside callChargeWithOtpUrl");
		HashMap<String, String> dbvalues = dbconfig.getValueFromDB();
		curlecUrl = dbvalues.get("curlec.url");
		if(paymentRequest.getClientType() == 1) {
		url = curlecUrl + "chargeNow?merchantId=" + dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID) + "&employeeId="
				+ dbvalues.get(GlobalConstants.AP_CURLEC_EMP_ID) + "&refNumber=" + paymentRequest.getRefNumber()
				+ "&collectionAmount=" + paymentRequest.getAmount() + "&invoiceNumber=" + paymentRequest.getBillCode()
				+ "-" + paymentRequest.getUniqueRequestNo() + "&collectionCallbackUrl=" + callBackUrl + "&redirectUrl="
				+ paymentRequest.getRedirectUrl() + "&method=chargenowOTP";
		}else if(paymentRequest.getClientType() == 2) {
			url = curlecUrl + "chargeNow?merchantId=" + dbvalues.get(GlobalConstants.MP_CURLEC_MERCHANT_ID) + "&employeeId="
					+ dbvalues.get(GlobalConstants.MP_CURLEC_EMP_ID) + "&refNumber=" + paymentRequest.getRefNumber()
					+ "&collectionAmount=" + paymentRequest.getAmount() + "&invoiceNumber=" + paymentRequest.getBillCode()
					+ "-" + paymentRequest.getUniqueRequestNo() + "&collectionCallbackUrl=" + callBackUrl + "&redirectUrl="
					+ paymentRequest.getRedirectUrl() + "&method=chargenowOTP";
		}
		log.info("URL in callChargeWithOtpUrl " + url);
		return url;
	}

	public InitResponseOutput callCurlecNewMandateService(InitMandate initMandate)
			throws URISyntaxException, ParseException, JSONException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		boolean message = false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Curlec_MandateResponse initResponsedb = new Curlec_MandateResponse();
		String effectiveDate = formatter.format(date);
		String url = "";
		String refNo = " ";
		ResponseEntity<String> response = null;
		InitResponseOutput initResponse = new InitResponseOutput();
		refNo = generateRefNo(initMandate.getClientType());
		HashMap<String, String> dbvalues = dbconfig.getValueFromDB();
		curlecUrl = dbvalues.get("curlec.url");
		if(initMandate.getClientType() == 1 ) {
		url = curlecUrl + "curlec-services/mandate?referenceNumber=" + refNo + "&effectiveDate=" + effectiveDate
				+ "&expiryDate=&amount=30000.00"
				+ "&frequency=MONTHLY&maximumFrequency=99&purposeOfPayment=Loans&businessModel=B2C" + "&name="
				+ initMandate.getNameOnCard().replace(" ", "%20") + "&emailAddress=" + initMandate.getEmail()
				+ "&phoneNumber=" + initMandate.getMobileNo() + "&idType=NRIC&idValue=" + initMandate.getIdValue()
				+ "&linkId=Notes" + "&merchantId=" + dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID) + "&employeeId="
				+ dbvalues.get(GlobalConstants.AP_CURLEC_EMP_ID) + "&method=04&paymentMethod=2";
		}else if(initMandate.getClientType() == 2) {
			url = curlecUrl + "curlec-services/mandate?referenceNumber=" + refNo + "&effectiveDate=" + effectiveDate
					+ "&expiryDate=&amount=30000.00"
					+ "&frequency=MONTHLY&maximumFrequency=99&purposeOfPayment=Loans&businessModel=B2C" + "&name="
					+ initMandate.getNameOnCard().replace(" ", "%20") + "&emailAddress=" + initMandate.getEmail()
					+ "&phoneNumber=" + initMandate.getMobileNo() + "&idType=NRIC&idValue=" + initMandate.getIdValue()
					+ "&linkId=Notes" + "&merchantId=" + dbvalues.get(GlobalConstants.MP_CURLEC_MERCHANT_ID) + "&employeeId="
					+ dbvalues.get(GlobalConstants.MP_CURLEC_EMP_ID) + "&method=04&paymentMethod=2";
		}

		log.info("Invoke curlec new mandate API :" + url);

		URI uri = new URI(url);

		try {
			response = restTemplate.postForEntity(uri, null, String.class);
			log.info("Curlec new mandate result status code :" + response.getStatusCodeValue());
			log.info("Curlec response :" + result);

			if (response != null && response.getStatusCodeValue() == 200) {
				if(initMandate.getClientType() == 1) {
				initResponse.setMerchantId(dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID));
				}else if(initMandate.getClientType() == 2) {
					initResponse.setMerchantId(dbvalues.get(GlobalConstants.MP_CURLEC_MERCHANT_ID));
				}
				initResponse.setResponseCode("00");
				String body = response.getBody();
				JSONObject bodyJSON = new JSONObject(body);
				log.info("bodyJSON " + bodyJSON);
				Object responseObj = bodyJSON.get("Response");
				JSONArray responseJSON = new JSONArray(responseObj.toString());
				Object responseObjFromArr = responseJSON.get(0);
				JSONObject responseJsonObj = new JSONObject(responseObjFromArr.toString());
				JSONArray sellerorder = responseJsonObj.getJSONArray("fpx_sellerOrderNo");
				JSONArray sellerExorder = responseJsonObj.getJSONArray("fpx_sellerExOrderNo");

				initResponse.setRefNumber(sellerorder.get(0).toString());
				initResponsedb.setRefNumber(sellerorder.get(0).toString());

				initResponse.setVgsNumber(sellerExorder.get(0).toString());
				initResponsedb.setVgsNumber(sellerExorder.get(0).toString());
				log.info("initResponse " + initResponse);
			} else if (response == null) {
				initResponse.setResponseCode("01");
				initResponsedb.setResponseCode("01");
				initResponse.setErrorMsg("Internal Server Error");
			}
		} catch (Exception e) {
			log.info("Exception while getting response :" + e.getLocalizedMessage());
			message = e.getLocalizedMessage().contains("Reference Number is in use!");
			if (message == true) {
				log.info("Reference Number is in use!");
				initResponse.setResponseCode("01");
				initResponsedb.setResponseCode("01");
				initResponse.setErrorMsg("Reference Number is in use");
			} else {
				initResponse.setResponseCode("01");
				initResponsedb.setResponseCode("01");
				initResponse.setErrorMsg(e.getLocalizedMessage());
			}
		}
		initResponsedb.setMerchantId(dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID));
		initResponsedb.setResponseCode("00");
		saveToDB.saveResponseToDB(initResponsedb);
		return initResponse;
	}

	private String generateRefNo(int clientType) throws IOException {
		String refNo = "";
		ReferenceNumber updateRef = new ReferenceNumber();
		ReadProperties properties = new ReadProperties();
		Properties prop = null;
		String referrnecnumber;
		if (clientType == 1) {
			String format = "";
			StringBuilder buffer = new StringBuilder();
			referrnecnumber = referenceNumberRepo.findValueByName(GlobalConstants.AP);
			log.info("referrnecnumber " + referrnecnumber);
			format = referrnecnumber;
			buffer.append(referrnecnumber);
			format = String.valueOf(Integer.parseInt(format) + 1);
			int tokenLength = format.length();
			String formatZero = buffer.toString().substring(0, buffer.toString().length() - tokenLength) + format;
			char[] format1 = formatZero.toCharArray();
			log.info("format1 " + format1);
			format = "AP";
			for (int j = 0; j < format1.length; j++) {
				format += format1[j];
			}
			refNo = format;
			log.info("refNo " + refNo);
			ReferenceNumber updateAP = referenceNumberRepo.getById(0);
			updateAP.setValue(refNo.substring(2));
			referenceNumberRepo.save(updateAP);
			log.info("Reference Number Updated in table");

		} else if (clientType == 2) {
			String format = "";
			StringBuilder buffer = new StringBuilder();
			referrnecnumber = referenceNumberRepo.findValueByName(GlobalConstants.MP);
			log.info("referrnecnumber " + referrnecnumber);
			format = referrnecnumber;
			buffer.append(referrnecnumber);
			format = String.valueOf(Integer.parseInt(format) + 1);
			int tokenLength = format.length();
			String formatZero = buffer.toString().substring(0, buffer.toString().length() - tokenLength) + format;
			char[] format1 = formatZero.toCharArray();
			log.info("format1 " + format1);
			format = "MP";
			for (int j = 0; j < format1.length; j++) {
				format += format1[j];
			}
			refNo = format;
			log.info("refNo " + refNo);
			ReferenceNumber updateAP = referenceNumberRepo.getById(1);
			updateAP.setValue(refNo.substring(2));
			referenceNumberRepo.save(updateAP);
			log.info("Reference Number Updated in table");
		}

		return refNo;
	}

	public ResponseEntity<String> checkCurlecStatus(String ccTransactionId) {
		String url = "";
		ResponseEntity<String> statusResponse = null;
		HashMap<String, String> dbvalues = dbconfig.getValueFromDB();
		curlecUrl = dbvalues.get("curlec.url");

		url = curlecUrl + "checkstatuscc";
		log.info("Invoke curlec to check collection status :" + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("ccTransactionId", ccTransactionId);
		map.add("merchantId", dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		statusResponse = restTemplate.postForEntity(url, request, String.class);

		log.info("Curlec collection status: " + statusResponse.getStatusCodeValue());
		log.info("Curlec collection status response: " + statusResponse);

		return statusResponse;
	}

	public ResponseEntity<String> callChargeNow(ChargeUserRequest paymentRequest) {
		String url = "";
		ResponseEntity<String> chargeNowResponse = null;
		HashMap<String, String> dbvalues = dbconfig.getValueFromDB();
		curlecUrl = dbvalues.get("curlec.url");
		url = curlecUrl + "curlec-services?method=60";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		log.info("Invoke curlec callNow : " + url);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("employeeId", dbvalues.get(GlobalConstants.AP_CURLEC_EMP_ID));
		map.add("merchantId", dbvalues.get(GlobalConstants.AP_CURLEC_MERCHANT_ID));
		map.add("collectionAmount", paymentRequest.getAmount());
		map.add("refNumber", paymentRequest.getRefNumber());
		map.add("invoiceNumber", paymentRequest.getBillCode() + "-" + paymentRequest.getUniqueRequestNo());
		map.add("collectionCallbackUrl", paymentRequest.getCallBackUrl());
		map.add("redirectUrl", paymentRequest.getRedirectUrl());
		log.info("Invoke curlec callNow with request body: " + map);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		chargeNowResponse = restTemplate.postForEntity(url, request, String.class);
		return chargeNowResponse;
	}

	/*
	 * public String CurlecResponse(PaymentRequest paymentRequest, String
	 * referrnecnumbers, String merchantID) throws Exception { ReadProperties
	 * properties = new ReadProperties(); Properties prop; String referrnecnumber =
	 * ""; if (!referrnecnumbers.isEmpty()) { referrnecnumber = referrnecnumbers; }
	 * else { // referrnecnumber =
	 * Files.readString(Paths.get("src//main//resources//refer.txt")); prop =
	 * properties.readPropertiesFile("src//main//resources//refer.properties");
	 * //String cardReference =
	 * Files.readString(Paths.get("src//main//resources//refer.txt"));
	 * referrnecnumber= prop.getProperty("Refer");
	 * //referrnecnumber=referrnecnumbers; } RestTemplate restTemplate = new
	 * RestTemplate();
	 * 
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date(); String effectiveDate = formatter.format(date); String url="";
	 * //Curlec if(paymentRequest.getHostType() == 1){ url =
	 * "https://demo.curlec.com/new-mandate?referenceNumber=" + referrnecnumber +
	 * "&merchantUrl=" + paymentRequest.getCallBackUrl() + "&merchantCallbackUrl=" +
	 * paymentRequest.getCallBackUrl() + "&effectiveDate=" + effectiveDate +
	 * "&expiryDate=&amount=" + paymentRequest.getAmount() // use amount variable
	 * from input +
	 * "&frequency=MONTHLY&maximumFrequency=99&purposeOfPayment=Loans&businessModel=B2C"
	 * + "&name=" + paymentRequest.getCustomerName() + "&emailAddress=" +
	 * paymentRequest.getLoginId() + "&phoneNumber=" + paymentRequest.getMobileNo()
	 * + "&idType=NRIC&idValue=8002270421111&bankId=19&linkId=Notes" +
	 * "&merchantId=447300&employeeId=44733&method=04&paymentMethod=2";
	 * 
	 * System.out.println("Curlec URL " +url); NewMandateRequestDto
	 * newMandateRequestDto = NewMandateRequestDto.builder()
	 * .url("https://demo.curlec.com/new-mandate") .referenceNumber(referrnecnumber)
	 * .purposeOfPayment("Loans") .phoneNumber(paymentRequest.getMobileNo())
	 * .paymentMethod("2") .name(paymentRequest.getCustomerName()) .method("04")
	 * .merchantUrl(paymentRequest.getCallBackUrl()) .merchantId("447300")
	 * .merchantCallbackUrl(paymentRequest.getCallBackUrl()) .maximumFrequency("99")
	 * .linkId("Notes") .idValue("8002270421111") .idType("NRIC")
	 * .frequency("MONTHLY") .expiryDate("") .employeeId("44733")
	 * .effectiveDate(effectiveDate) .createdAt(new Date()) .businessModel("B2C")
	 * .emailAddress(paymentRequest.getLoginId()) .bankId("19") //
	 * .amount(paymentRequest.getAmount()) .build(); //
	 * newMandateRequestDtoEntityRepository.save(newMandateRequestDto); }else { //
	 * String carddetails=paymentRequest.getCarddetails().split("#")[0]+"" url =
	 * "https://paydee.gomobi.io/UMEzyway/TEzywayReqDetails.jsp?mobiApiKey=" +
	 * referrnecnumber + "&service=UMECOMAPI_SALE_RE" + "&nameOnCard=" +
	 * paymentRequest.getCustomerName() + "&orderId=" + paymentRequest.getUniqueId()
	 * + "&email=" + paymentRequest.getLoginId() + "&effectiveDate=" + effectiveDate
	 * + "&cardNo="+ paymentRequest.getCarddetails().split("#")[0] + "&cvv="+
	 * paymentRequest.getCarddetails().split("#")[1] + "&expiryDate="+
	 * paymentRequest.getCarddetails().split("#")[2] + "&amount=" +
	 * paymentRequest.getCarddetails().split("#")[3] + "&loginId="+
	 * paymentRequest.getLoginId() +
	 * "&umResponseUrl="+paymentRequest.getCallBackUrl() +
	 * "&uid="+paymentRequest.getCustId() + "&mobileNo=" +
	 * paymentRequest.getMobileNo() + "&saveCard=" + paymentRequest.getSaveCard() +
	 * "&subMerchantMid=" +paymentRequest.getMerchantId();
	 * 
	 * System.out.println("Curlec URL in else " +url); MobyversaNewMandateRequestDto
	 * newMandateRequestDto = MobyversaNewMandateRequestDto.builder()
	 * .url("https://paydee.gomobi.io/UMEzyway/TEzywayReqDetails.jsp")
	 * .referenceNumber(referrnecnumber) .service("UMECOMAPI_SALE_RE")
	 * .name(paymentRequest.getCustomerName())
	 * .phoneNumber(paymentRequest.getMobileNo()) .effectiveDate(effectiveDate)
	 * .cardNo(paymentRequest.getCarddetails().split("#")[0])
	 * .cvv(paymentRequest.getCarddetails().split("#")[1])
	 * .expiryDate(paymentRequest.getCarddetails().split("#")[2])
	 * .amount(paymentRequest.getCarddetails().split("#")[3])
	 * .loginId(paymentRequest.getLoginId())
	 * .merchantCallbackUrl(paymentRequest.getCallBackUrl())
	 * .orderId(paymentRequest.getUniqueId()) .uid(paymentRequest.getCustId())
	 * .mobileNo(paymentRequest.getMobileNo())
	 * .saveCard(paymentRequest.getSaveCard())
	 * .merchantId(paymentRequest.getMerchantId()) .build(); //mobyversaflag=true;
	 * // mobyversaMandateRequestDtoEntityRepository.save(newMandateRequestDto); }
	 * 
	 * 
	 * log.info("Invoked curlec new mandate API :"+url);
	 * 
	 * URI uri = new URI(url);
	 * 
	 * try { result = restTemplate.postForEntity(uri, null, String.class);
	 * 
	 * log.info("Curlec new mandate result status code :"+result.getStatusCodeValue(
	 * )); System.out.println("Curlec response :"+result);
	 * System.out.println("Reference number:"+referrnecnumber); //
	 * writeReferenceNumber(referrnecnumber); return url;
	 * 
	 * } catch (Exception e) {
	 * System.out.println("Exception while getting response :"+e); String subMessage
	 * = StringUtils.substringBetween(e.getLocalizedMessage(), "<message>",
	 * "</message>"); boolean message =
	 * subMessage.contains("Reference Number is in use!");
	 * 
	 * log.info("Curlec response "+subMessage+" for url"+url);
	 * 
	 * if (message == true) { String format = ""; StringBuilder buffer = new
	 * StringBuilder(); //
	 * referrnecnumber=referrnecnumber.substring(0,1).split("-"); String[] arr =
	 * referrnecnumber.substring(2, referrnecnumber.length()).split("-"); for (int i
	 * = 0; i < arr.length; i++) { buffer.append(arr[i]); } format =
	 * buffer.toString();
	 * 
	 * format = String.valueOf(Integer.parseInt(format) + 1); int tokenLength =
	 * format.length(); String formatZero = buffer.toString().substring(0,
	 * buffer.toString().length() - tokenLength) + format; char[] format1 =
	 * formatZero.toCharArray(); format = "MY"; for (int j = 0; j < format1.length;
	 * j++) { if (j == 4 || j == 7) { format += "-"; }
	 * 
	 * format += format1[j]; }
	 * log.info("Retry curlec new mandate with new reference "+format);
	 * CurlecResponse(paymentRequest, format,merchantID); } else {
	 * log.error("Curlec API not responding due to "+subMessage); return null; } }
	 * return url; }
	 */
	private void writeAPReferenceNumber(String content) {

		try {
			content = content.replace("P", "P=");
			log.info("Write latest generated reference number +" + content);
			Files.write(Paths.get("/var/tmp/referenceNumber_ap.txt"), content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeMPReferenceNumber(String content) {

		try {
			content = content.replace("P", "P=");
			log.info("Write latest generated reference number +" + content);
			Files.write(Paths.get("/var/tmp/referenceNumber_mp.txt"), content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ResponseEntity<String> callCurlecCallback(CurlecCallback curlecCallback, String merchantCallbackUrl) {
		String url = "";
		ResponseEntity<String> callbackResponse = new ResponseEntity(HttpStatus.OK);
		try {
			if (merchantCallbackUrl != null && !merchantCallbackUrl.isBlank()) {

				url = merchantCallbackUrl;
				log.info("Invoking merchant site to relay callback response : " + url);
				List<MediaType> listM = new ArrayList<>();
				listM.add(MediaType.APPLICATION_JSON);
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(listM);
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<CurlecCallback> request = new HttpEntity<CurlecCallback>(curlecCallback, headers);
				log.info("Request to merchant callback : " + request);
				log.info("curlecCallback Request to merchant callback : " + request.getBody());
				callbackResponse = restTemplate.postForEntity(url, request, String.class);
				log.info("Response from merchant callback : " + callbackResponse);
			} else {
				log.info(" Cannot invoke merchant site as callbackUrl is empty..!");

			}
		} catch (HttpClientErrorException e) {
			log.info(" Exception from merchant server " + e.getLocalizedMessage());

		}
		return callbackResponse;

	}

	@Override
	public String getCurlecCallbackUrl(CurlecCallback curlecCallbackReq) {
		if (curlecCallbackReq != null && !ObjectUtils.isEmpty(curlecCallbackReq)) {
			String uniqueRequestNo = curlecCallbackReq.getInvoiceNumber().split("-")[1];
			ChargeUserRequest callbackUrl = chargeUserRequestEntityRepository
					.findByrefNumberAndBillCodeAndUniqueRequestNo(curlecCallbackReq.getRefNumber(), curlecCallbackReq.getBillCode(),uniqueRequestNo);
			if (callbackUrl != null && StringUtils.isNotBlank(callbackUrl.getCallBackUrl())) {
				return callbackUrl.getCallBackUrl();

			}
		}
		return null;
	}

}
