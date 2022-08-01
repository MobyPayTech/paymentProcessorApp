package com.mobpay.Payment.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "paymentprocessor_sysconfig")
@Data
public class PaymentProcessorsysconfig implements Serializable{
	
	private static final long serialVersionUID = 7156526077883281623L;
	
	   @Id
	    @Column(name = "name")
	    private String name;
	    @Column(name = "value")
	    private String value;
	    @Column(name = "description")
	    private String description;
	    @Column(name = "createdAt")
	    private Date createdAt;
	    @Column(name = "updatedAt")
	    private String updatedAt;
}
