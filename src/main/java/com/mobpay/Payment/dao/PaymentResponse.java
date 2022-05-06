package com.mobpay.Payment.dao;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PaymentAPI3_Response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;
    private String mid;
    private String datetime;
    private String cardFirstDigits;
    private String cardLastDigits;
    private String cardHolderName;
    private float amount;
    private String responseDescription;
    @Column(name = "OrderId")
    private String orderId;
    private String responseCode;
    private String responseMessage;
    private String tokenId;
    private String cardBrand;
    private String cardType;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;
    @Column(name = "BillCode")
    private String billCode;
    private String transactionId;
    private String cardReference;
   

}
