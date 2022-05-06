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

@Entity
@Table(name = "payment_new_mandate_response")
@Data
@Builder
@AllArgsConstructor
public class MobiversaSaveCardPaymentResponse {

	public MobiversaSaveCardPaymentResponse() {
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
	@Column(name = "trxId")
	private String trxId;
	@Column(name = "amount")
	private float amount;
	@Column(name = "responseCode")
	private String responseCode;
	@Column(name = "tid")
	private String tid;
	@Column(name = "mid")
	private String mid;
	@Column(name = "rrn")
	private String rrn;
	@Column(name = "date")
	private String date;
	@Column(name = "time")
	private String time;
	@Column(name = "approveCode")
	private String approveCode;
	@Column(name = "cardNo")
	private String cardNo;
	@Column(name = "cardHolderName")
	private String cardHolderName;
	@Column(name = "txnType")
	private String txnType;
	@Column(name = "invoiceId")
	private String invoiceId;
	@Column(name = "responseMsg")
	private String responseMsg;
	@Column(name = "CreatedAt")
	private Date createdAt;
	@Column(name = "UpdatedAt")
	private Date updatedAt;

}
