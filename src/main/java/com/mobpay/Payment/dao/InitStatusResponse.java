package com.mobpay.Payment.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_new_mandate_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitStatusResponse {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Id")
	    private Integer id;
	 	private String merchantId ;
	 	private String dateTime;
	 	private String cardFirstDigits;
	 	private String cardLastDigits;
	 	private String cardholderName;
	    private Double amount;
	    private String responseDesc;
	    private String billCode;
	    private String responseCode;
	    private String responseMsg;
	 	private String paymentResCode;
	 	private String tokenId;
	 	private String cardBrand;
	 	private String cardType;
}
