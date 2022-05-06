package com.mobpay.Payment.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "subsequeut_payment_response")
public class SubsequentPaymentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    private String responseCode;
    private String responseDescription;
    private String billCode;
    private float amount;
    private String transactionId;
    private String dateTime;
}
