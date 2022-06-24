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
@Table(name = "pp_mobiversa_savecard_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiversaSaveCardPaymentRequest {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Id")
	    private Integer id;
	private String walletId;
    private String amount;
    private String mobileNo;
  //  @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceId")
    private String invoiceId;
    private String service;
}
