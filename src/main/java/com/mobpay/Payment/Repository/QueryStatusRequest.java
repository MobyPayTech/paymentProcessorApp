package com.mobpay.Payment.Repository;

import lombok.Data;

@Data
public class QueryStatusRequest {
	
	private Integer clientType;
	private String billCode;
	private Integer statusCode;

}
