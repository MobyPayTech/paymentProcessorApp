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

@Entity
@Table(name = "Payment_callback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurlecCallbackResponse {

	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Id")
	    private Integer id;
	    @Column(name = "callbackUrl")
	    private String callbackUrl;
	    @Column(name = "redirectUrl")
	    private String redirectUrl;
	    @Column(name = "status")
	    private String status;
}
