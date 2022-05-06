package com.mobpay.Payment.dao;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "curlec_chargenow_result")
public class CurlecChargeNowResultDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    private String cardReference;
    private String status;
    private String transactionId;
    private String dateTime;
    @Type(type="text")
    private String responsePayload;

}
