package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequeryRequest {

	private String merchantId;
	private String invoiceNumber;
	private String methodNumber;

}
