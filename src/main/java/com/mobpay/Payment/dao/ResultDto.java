package com.mobpay.Payment.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto {
    private String responseCode;
    private String responseDescription;
    private String responseMessage;
}
