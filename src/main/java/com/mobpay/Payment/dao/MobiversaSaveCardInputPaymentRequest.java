package com.mobpay.Payment.dao;

import java.util.Date;

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
@Table(name = "payment_mandate_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiversaSaveCardInputPaymentRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "WalletId")
    private String walletId;
    @Column(name = "amount")
    private String amount;
    @Column(name = "MobileNo")
    private String mobileNo;
    @NotEmpty
    @Column(name = "BillCode")
    private String billCode;
    @Column(name = "Frequency")
    private String frequency;
    private String serviceName;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;
}