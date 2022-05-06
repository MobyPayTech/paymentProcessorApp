package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_new_mandate_request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewMandateRequestDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @Column(name="url",columnDefinition="LONGTEXT")
    private String url;
    private String referenceNumber;
    private String merchantUrl;
    private String merchantCallbackUrl;
    private String effectiveDate;
    private String expiryDate;
    private double amount;
    private String frequency;
    private String maximumFrequency;
    private String purposeOfPayment;
    private String businessModel;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private String idType;
    private String idValue;
    private String bankId;
    private String linkId;
    private String merchantId;
    private String employeeId;
    private String method;
    private String paymentMethod;
    private Date createdAt;
    private String billCode; 
    private String transactionId; 
}
