package com.mobpay.Payment.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "remove_card_request")
public class RemoveCardRequestDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @NotNull
    private String serviceName;
    @NotNull
    private String custId;
    @NotNull
    private String cardReference;
    @NotNull
    private String mobileNo;
    @NotNull
    private String hostType;
    private Date createdAt;
    private Date updatedAt;

    @Column(name = "clientType")
    private String clientType;
}
