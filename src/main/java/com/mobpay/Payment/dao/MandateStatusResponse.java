package com.mobpay.Payment.dao;

import lombok.Data;

@Data
public class MandateStatusResponse {

	private int responseCode;
	private String responseMsg;
	private String fpx_sellerExOrderNo;
	private String fpx_buyerBankId;
	private String fpx_txnCurrency;
	private String fpx_collectionAmount;
	private String fpx_type;
	private String fpx_fpxTxnTime;
	private String fpx_sellerOrderNo;
	private String enrolment_status;
	private String fpx_buyerName;
	private String fpx_sellerId;
	private String fpx_debitAuthCode;
	private String fpx_txnAmount;
	private String fpx_fpxTxnId;
	private String fpx_effectiveDate;
	private String fpx_expiryDate;
	private String enrolment_condition;
	private String max_collection;

public MandateStatusResponse() {
	
}
	
}
