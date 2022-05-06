package com.mobpay.Payment.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
//@Builder
@Entity
@Table(name = "mobi_callback_data")
public class MobiCallBackRespositoryDAO {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
 @Column(name = "aid")
    private String Aid;
 @Column(name = "amount")
    private String Amount;
 @Column(name = "approveCode")
    private String ApproveCode;
 @Column(name = "cardHolderName")
    private String CardHolderName;
 @Column(name = "cardNo")
    private String CardNo;
 @Column(name = "date")
    private String Date;
 @Column(name = "mid")
    private String Mid;
 @Column(name = "orderId")
    private String OrderId;
 @Column(name = "responseCode")
    private String ResponseCode;
 @Column(name = "responseDescription")
    private String ResponseDescription;
 @Column(name = "responseMessage")
    private String ResponseMessage;
 @Column(name = "tid")
    private String Tid;
 @Column(name = "uid")
    private String Uid;
 @Column(name = "walletId")
    private String WalletId;
 @Column(name = "rrn")
    private String Rrn;
 @Column(name = "time")
    private String Time;
}
