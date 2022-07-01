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
@Table(name = "pp_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;
	@Column(name = "ip_address")
	private String ip_address;
	@Column(name = "client_id")
	private String client_id;
	@Lob
	@Column(name = "request")
	private String request;
	@Lob
	@Column(name = "response")
	private String response;
	@Column(name = "timestamp")
	private Date timestamp;

}
