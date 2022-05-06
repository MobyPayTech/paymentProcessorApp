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

		  
	  String refNumber;
	  String billCode;
	  String ccTransactionId;
	  String collectionStatus;
}

