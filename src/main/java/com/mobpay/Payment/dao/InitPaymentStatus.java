package com.mobpay.Payment.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_new_mandate_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitPaymentStatus {


	
  /*  @Column(name = "Id")
    private Integer id;*/
    @Column(name = "ServiceName")
    private String serviceName;
    @Column(name = "BillCode")
    private String billCode;
    @Column(name = "MobileNo")
    private String mobileNo;
    @Column(name = "amount")
    private Double amount;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private String transactionId;
    // @Column(name = "NameOnCard")
    // private String nameOnCard;
    @Column(name = "HostType")
    private int hostType;
    @Column(name = "clientType")
    private int clientType;
}
