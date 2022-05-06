package com.mobpay.Payment.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class CurelecChargeNowSuccessResponse {

    // {"Status":["200"],
    // "Response":[{"collection_status":["SUCCESSFULLY_COMPLETE"],"method":["CHARGENOW"],"cc_transaction_id":["xaf-11012022-23*13"],"reference_number":["MY0000-000-038"],"invoice_number":["INV-001"]}],
    // "Date":["Thu Jan 13 01:16:20 MYT 2022"]}

    @JsonProperty("collection_status")
    private List<String> collectionStatus;
    @JsonProperty("method")
    private List<String> method;
    @JsonProperty("cc_transaction_id")
    private List<String> ccTransactionId;
    @JsonProperty("reference_number")
    private List<String> referenceNumber;
    @JsonProperty("invoice_number")
    private List<String> invoiceNumber;


}
