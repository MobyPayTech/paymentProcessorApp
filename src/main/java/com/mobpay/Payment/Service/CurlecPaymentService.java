package com.mobpay.Payment.Service;

import com.mobpay.Payment.dao.CurlecCallback;

public interface CurlecPaymentService {
	
	public String getCurlecCallbackUrl(CurlecCallback curlecCallbackReq);

}
