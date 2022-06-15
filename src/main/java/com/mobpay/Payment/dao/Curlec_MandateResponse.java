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
@Table(name = "Curlec_MandateResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curlec_MandateResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;
    private String errorMsg;
    @Column(name = "MerchantId")
    private String merchantId;
    private String responseCode;
    @Column(name = "refNumber")
    private String refNumber;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;
    @Column(name = "VgsNumber")
    private String vgsNumber;
}
