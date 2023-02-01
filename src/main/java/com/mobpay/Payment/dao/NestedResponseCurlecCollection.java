package com.mobpay.Payment.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedResponseCurlecCollection {

	@JsonProperty("response_batch")
	private List<String> responseBatch;
	@JsonProperty("collection_date")
	private List<String> collectionDate = null;
	@JsonProperty("transaction_notes")
	private List<String> transactionNotes;
	@JsonProperty("batch_id")
	private List<String> batchId;
	@JsonProperty("response_date")
	private List<String> responseDate = null;
	@JsonProperty("mandate_reference")
	private List<String> mandateReference;
	@JsonProperty("batch_collection_status_code")
	private List<String> batchCollectionStatusCode;
	@JsonProperty("transaction_type")
	private List<String> transactionType;
	@JsonProperty("collection_status")
	private List<String> collectionStatus;
	@JsonProperty("collection_notes")
	private List<String> collectionNotes;
	@JsonProperty("collection_amount")
	private List<String> collectionAmount;
	@JsonProperty("cc_authorisation_code")
	private List<String> ccAuthorisationCode;
	@JsonProperty("max_amount")
	private List<String> maxAmount;
	@JsonProperty("cc_transaction_id")
	private List<String> ccTransactionId;
	@JsonProperty("batch_collection_date")
	private List<String> batchCollectionDate;
	@JsonProperty("transaction_reference")
	private List<String> transactionReference;
	@JsonProperty("collection_description")
	private List<String> collectionDescription;
	@JsonProperty("collection_reference")
	private List<String> collectionReference;
	@JsonProperty("collection_status_code")
	private List<String> collectionStatusCode;
	@JsonProperty("batch_collection_event")
	private List<String> batchCollectionEvent;
}
