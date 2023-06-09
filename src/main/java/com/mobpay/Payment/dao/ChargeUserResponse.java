package com.mobpay.Payment.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pp_curlec_chargeUserResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeUserResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;
	@Lob
	@Column(name = "chargeNowWithOtpUrl")
	private String chargeNowWithOtpUrl;
	private String responseMessage;
	private String responseCode;
	@Column(name = "collection_status")
	private String collection_status;
	@Column(name = "method")
	private String method;
	@Column(name = "billCode")
	private String billCode;
	@Column(name = "invoiceNumber")
	private String invoiceNumber;
	@Column(name = "ccTransactionId")
	private String ccTransactionId;
	@Column(name = "refNumber")
	private String refNumber;
	@Column(name = "CreatedAt")
	private Date createdAt;
	@Column(name = "UpdatedAt")
	private Date updatedAt;

}
