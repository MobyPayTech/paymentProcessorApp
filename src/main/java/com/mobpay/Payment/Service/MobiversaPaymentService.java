package com.mobpay.Payment.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mobpay.Payment.dao.InitPayment;
import com.mobpay.Payment.dao.MobiversaSaveCardInputPaymentRequest;
import com.mobpay.Payment.dao.MobiversaSaveCardPaymentRequest;
import com.mobpay.Payment.dao.NewMandateRequestDto;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.VGSInput;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class MobiversaPaymentService {

	//@Autowired
	MobiversaSaveCardPaymentRequest saveCardPaymentRequest = new MobiversaSaveCardPaymentRequest();
	
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> result = null;

	public ResponseEntity<String> callMobiversaService(PaymentRequest paymentRequest)
			throws URISyntaxException, ParseException, JSONException {

		RestTemplate restTemplate = new RestTemplate();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String effectiveDate = formatter.format(date);
		String url = "";
		// Mobiversa
		if (paymentRequest.getHostType() == 2) {
		//	url = "https://paydee.gomobi.io/UMEzyway/TEzywayReqDetails.jsp?mobiApiKey=b07ad9f31df158edb188a41f725899bc&service=UMECOMAPI_SALE_RE&ip=175.143.200.41&mUrl=https://airapay.my&firstName=Sandbox&nameOnCard=Shiva%20Guru%20Balaji%20S&lastName=454243&postalCode=47100&shippingState=Selangor&orderId=A220408192509-OC01002-798-826491&email=edw54185@mzico.com&carddetails=7671674975706d7a396f694364366441596363324d545431364742304b424633795769443878546c2b4842633056366a7a66386f56452b437555586564325450&loginId=Mobiversa&umResponseUrl=https://sandbox.app.airapay.my/payment/complete&uid=A220408192509-OC01002-798-826491&mobileNo=05418554185&saveCard=YES";

			url = "https://paydee.gomobi.io/UMEzyway/TEzywayReqDetails.jsp?mobiApiKey=b07ad9f31df158edb188a41f725899bc&service=UMECOMAPI_SALE_RE&ip="+paymentRequest.getIp()+"&mUrl=" +paymentRequest.getRedirectUrl() +"&firstName=" +paymentRequest.getCustomerName() + "&nameOnCard=" +paymentRequest.getNameOnCard() + "&postalCode=" +paymentRequest.getPostalCode() +"&shippingState=" +paymentRequest.getShippingState() + "&orderId=" +paymentRequest.getBillCode()+"&email=" +paymentRequest.getLoginId() + "&carddetails=" +paymentRequest.getCarddetails() +"&umResponseUrl="+paymentRequest.getCallBackUrl()+"&uid=" +paymentRequest.getRef1()+ "&mobileNo=" +paymentRequest.getMobileNo() + "&loginId=Mobiversa&saveCard=YES";  
			log.info("Invoke Mobiversa API :" + url);

			URI uri = new URI(url);

			try {
				result = restTemplate.getForEntity(uri, String.class);
				// result= restTemplate.exchange(uri,String.class);
				log.info("Mobiversa  result status code :" + result.getStatusCodeValue());
				log.info("Mobiversa response :" + result);

				return result;

			} catch (Exception e) {
				System.out.println("Exception while getting response :" + e);
			}

		}
		return result;
	}

	public ResponseEntity<String> callMobiversaSaveCardPaymentService(MobiversaSaveCardInputPaymentRequest inputRequest) {

		RestTemplate restTemplate = new RestTemplate();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String effectiveDate = formatter.format(date);
		String url = "";

		try {
			url = "https://paydee.gomobi.io/payment/ezywayservice/jsonservice";

			log.info("Invoke Mobiversa API :" + url);
			URI uri = new URI(url);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			headers.add("userName", "Mobiversa");
			headers.add("mobiApiKey", "b07ad9f31df158edb188a41f725899bc");
			
			saveCardPaymentRequest.setWalletId(inputRequest.getWalletId());
			saveCardPaymentRequest.setAmount(inputRequest.getAmount().toString());
			saveCardPaymentRequest.setMobileNo(inputRequest.getMobileNo());
			saveCardPaymentRequest.setInvoiceId(inputRequest.getBillCode() + "-" + inputRequest.getFrequency() );
			log.info("Invoice ID to Mobiversa " +saveCardPaymentRequest.getInvoiceId());
			saveCardPaymentRequest.setService("WALLET_DIRECT");
			log.info("Amount 2" +saveCardPaymentRequest.getAmount());
			HttpEntity<MobiversaSaveCardPaymentRequest> httpEntity = new HttpEntity<>(saveCardPaymentRequest, headers);
			log.info("httpEntity: " +httpEntity.getBody());
			result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
			log.info("Mobiversa new mandate result status code :" + result.getStatusCodeValue());
			log.info("Mobiversa response :" + result);

			return result;

		} catch (URISyntaxException e) {
			log.error("Exception:" + e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Exception:" + e.getLocalizedMessage());
		}

		return result;
	}
}
