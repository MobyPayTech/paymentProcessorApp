package com.mobpay.Payment.dao;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "Mobipayment_mandate_response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiversaPaymentResponse {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Id")
	    private Integer id;
 private String aid;
 private String amount;
 private String approveCode;
 private String cardHolderName;
 private String cardNo;
 private Date date;
 private String mid;
 private String orderId;
 private String responseCode;
 private String responseDescription;
 private String responseMessage;
 private String Rrn;
 private String tid;
 private LocalDateTime Time;
 private String trxId;
 private String uid;
 private String walletId;
 private String cardBrand;
 private String cardType;
 private String currency;
 private String mercMdr;
 private String countryCode;
 
}
