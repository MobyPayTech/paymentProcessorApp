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
@Table(name = "pp_curlec_MandateRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitMandate {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "ServiceName")
    private String serviceName;
    @NotEmpty
    @Column(name = "Email")
    private String email;
    @Column(name = "MobileNo")
    private String mobileNo;
    @Column(name = "NameOnCard")
    private String nameOnCard;
    @Column(name = "idValue")
    private String idValue;
    @Column(name = "clientType")
    private int clientType;
    @Column(name = "CreatedAt")
    private Date createdAt;
    @Column(name = "UpdatedAt")
    private Date updatedAt;

}
