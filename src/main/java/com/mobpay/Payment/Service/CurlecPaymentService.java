package com.mobpay.Payment.Service;

import org.springframework.http.ResponseEntity;

import com.mobpay.Payment.Repository.CollectionStatusRequest;
import com.mobpay.Payment.dao.CurlecCallback;
import com.mobpay.Payment.dao.CurlecVoid;
import com.mobpay.Payment.dao.RequeryRequest;

public interface CurlecPaymentService {
	
	public String getCurlecCallbackUrl(CurlecCallback curlecCallbackReq);
	
	public ResponseEntity<String> curlecVoid(CurlecVoid ccVoid);
	
	public ResponseEntity<String> curlecCollectionStatus(RequeryRequest ccVoid);

}
