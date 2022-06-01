package com.mobpay.Payment.dao;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeUserResponseOutput {

	private String chargeNowWithOtpUrl;
	private String errorMsg;
	private String responseCode;

	private String collection_status;
	private String billCode;
	private String invoiceNumber;
	private String ccTransactionId;
	private String refNumber;
	
	private Date createdAt;
	private Date updatedAt;

}
