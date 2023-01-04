package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurlecVoid {
	
	 private String ccTransactionId;
	  private String merchantId;
	  private String invoiceNumber;
	  private String employeeId;
	  private String reason;

}
