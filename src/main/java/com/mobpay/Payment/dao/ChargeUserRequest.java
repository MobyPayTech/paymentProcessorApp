package com.mobpay.Payment.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pp_curlec_chargeUserRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeUserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "amount")
    private float amount;
    @Lob
    @Column(name = "CallBackURL")
    private String callBackUrl;
    @Lob
    @Column(name = "redirectUrl")
    private String redirectUrl;
    @Column(name = "BillCode")
    private String billCode;
    @Column(name = "clientType")
    private int clientType;
    @Column(name = "withOtp")
    public Boolean withOtp;
    @Column(name = "refNumber")
    private String refNumber;
    @Column(name = "uniqueRequestNo")
    private String uniqueRequestNo;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;
    @Column(name = "authorizeRM1")
    private Boolean authorizeRM1;
    
    
}
