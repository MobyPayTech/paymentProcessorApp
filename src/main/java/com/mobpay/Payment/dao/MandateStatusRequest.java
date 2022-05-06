package com.mobpay.Payment.dao;

import lombok.Data;

@Data
public class MandateStatusRequest {

	    private String serviceName;
	    private String refNo;
	    private String mobileNo;
	    private String billCode;
	    private String txnId;
	    private int hostType;
	    private int clientType;
}
