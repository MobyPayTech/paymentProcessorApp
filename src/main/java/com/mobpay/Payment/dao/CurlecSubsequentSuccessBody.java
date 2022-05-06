package com.mobpay.Payment.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CurlecSubsequentSuccessBody {

    // {"Status":["200"],
    // "Response":[{"collection_status":["SUCCESSFULLY_COMPLETE"],"method":["CHARGENOW"],"cc_transaction_id":["xaf-11012022-23*13"],"reference_number":["MY0000-000-038"],"invoice_number":["INV-001"]}],
    // "Date":["Thu Jan 13 01:16:20 MYT 2022"]}

    @JsonProperty("Status")
    private List<String> status;
    @JsonProperty("Response")
    private List<CurelecChargeNowSuccessResponse> response;
    @JsonProperty("Date")
    private List<String> date;
    @JsonProperty("Message")
    private List<String> message;
}
