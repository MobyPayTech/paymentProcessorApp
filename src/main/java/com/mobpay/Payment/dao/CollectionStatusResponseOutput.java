package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionStatusResponseOutput {

	private String collection_status;
	private String cc_transaction_id;
	private String reference_number;
	private String responseCode;
	private String errorMsg;

}
