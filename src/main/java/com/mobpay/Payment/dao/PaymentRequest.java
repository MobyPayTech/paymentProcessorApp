package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pp_mobiversa_paymentrequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "ServiceName")
    private String serviceName;
    @Column(name = "LoginId")
    private String loginId;
    @Column(name = "MobileNo")
    private String mobileNo;
    @Column(name = "CustomerName")
    private String customerName;
    @Column(name = "NameOnCard")
    private String nameOnCard;
    @Column(name = "HostType")
    private int hostType;
    @Column(name = "IP")
    private String ip;
    @Column(name = "PostCode")
    private String postalCode;
    @Column(name = "ShippingState")
    private String shippingState;
    @Column(name = "BillCode")
    private String billCode;
    @Column(name = "CardDetails")
    private String carddetails;
    @Column(name = "CallBackURL")
    private String callBackUrl;
    @Column(name = "Response")
    private String Response;
    @Column(name = "Latitude")
    private String latitude;
    @Column(name = "Longitude")
    private String longitude;
    @Column(name = "SessionId")
    private String sessionId;
    @Column(name = "Reference1")
    private String ref1;
    @Column(name = "Reference2")
    private String ref2;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;
    @Column(name = "MerchantId")
    private String merchantId;
    @Column(name = "SaveCard")
    private String saveCard;
    @Column(name = "cardRef")
    private String cardRef;
    @Column(name = "cardBrand")
    private String cardBrand;
    @Column(name = "cardType")
    private String cardType;
    @Column(name = "custId")
    private String custId;
    @Column(name = "clientType")
    private int clientType;
    @Column(name = "uniqueId")
    private String uniqueId;
    @Column(name = "amount")
    private String amount;
    @Column(name = "redirectUrl")
    private String redirectUrl;
   


}
