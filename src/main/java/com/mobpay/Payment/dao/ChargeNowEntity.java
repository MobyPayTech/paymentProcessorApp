package com.mobpay.Payment.dao;

import com.mobpay.Payment.Repository.CollectionStatusRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeNowEntity {
	private String collectionAmount;
	private String refNumber;
	private String invoiceNumber;
	private String merchantId;
	private String employeeId;
	
}
