package com.mobpay.Payment.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurlecCallback {

		  
	  private String refNumber;
	  private String billCode;
	  private String invoiceNumber;
	  private String ccTransactionId;
	  private String collectionStatus;
	  private String callBackURL;
}

