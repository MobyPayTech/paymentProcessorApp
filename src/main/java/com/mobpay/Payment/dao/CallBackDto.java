package com.mobpay.Payment.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "curlec_callback_data")
public class CallBackDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    private String curlec_method;
    private String fpx_fpxTxnId;
    private String fpx_sellerExOrderNo;
    private String fpx_fpxTxnTime;
    private String fpx_sellerOrderNo;
    private String fpx_sellerId;
    private String fpx_txnCurrency;
    private String fpx_txnAmount;
    private String fpx_buyerName;
    private String fpx_buyerBankId;
    private String fpx_debitAuthCode;
    private String fpx_type;
    private String fpx_notes;
}
