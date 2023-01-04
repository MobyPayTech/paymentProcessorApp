package com.mobpay.Payment.Service;

public enum CurlecMethod {
	
	CURLEC_CALLBACK_WITHOTP("1"), CURLEC_CALLBACK_WITHOUTOTP("2");
	
	public final String label;

    private CurlecMethod(String label) {
        this.label = label;
    }

}
