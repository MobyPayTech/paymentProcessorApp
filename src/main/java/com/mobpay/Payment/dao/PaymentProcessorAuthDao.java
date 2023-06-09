package com.mobpay.Payment.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Entity
@Table(name = "paymentprocessor_auth")
@Data
public class PaymentProcessorAuthDao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "client_name")
	private String client_name;
	@Column(name = "api_key")
	private String api_key;
	@Column(name = "api_secret")
	private String api_secret;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "platform")
	private PlatformEnum platform;
	@Column(name = "version")
	private String version;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "lastUsedDate")
	private Date lastUsedDate;
	@Column(name = "updatedDate")
	private Date updatedDate;
}
