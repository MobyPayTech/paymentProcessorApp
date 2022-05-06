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
public class MobiversaSaveCardPaymentRequest {

	private String walletId;
    private String amount;
    private String mobileNo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceId")
    private String invoiceId;
    private String service;
}
