package com.mobpay.Payment.dao;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitResponseOutput {

    private String errorMsg;
    private String merchantId;
    private String responseCode;
    private String refNumber;
    private String vgsNumber;
}
