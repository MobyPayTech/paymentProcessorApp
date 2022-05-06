package com.mobpay.Payment.dao;

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

@Data
@Entity
@Table(name = "mobyversa_mandate_request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobyversaNewMandateRequestDto {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Id")
	    private Integer id;
	    @Column(name="url",columnDefinition="LONGTEXT")
	    private String url;
	    private String referenceNumber;
	    private String merchantUrl;
	    private String service;
	    private String orderId;
	    private String merchantCallbackUrl;
	    private String effectiveDate;
	    private String cardNo;
	    private String cvv;
	    private String expiryDate;
	    private String uid;
	    private String amount;
	    private String loginId;
	    private String mobileNo;
	    private String saveCard;
	    private String frequency;
	    private String maximumFrequency;
	    private String purposeOfPayment;
	    private String businessModel;
	    private String name;
	    private String emailAddress;
	    private String phoneNumber;
	    private String idType;
	    private String idValue;
	    private String bankId;
	    private String linkId;
	    private String merchantId;
	    private String employeeId;
	    private String method;
	    private String paymentMethod;
	    private Date createdAt;
}
