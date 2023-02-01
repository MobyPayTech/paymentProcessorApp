package com.mobpay.Payment.dao;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResponse {

	@JsonProperty("response_batch")
	public List<String> responseBatch;
	@JsonProperty("collection_date")
	public List<String> collectionDate = null;
	@JsonProperty("transaction_notes")
	public List<String> transactionNotes;
	@JsonProperty("batch_id")
	public List<String> batchId;
	@JsonProperty("response_date")
	public List<String> responseDate = null;
	@JsonProperty("mandate_reference")
	public List<String> mandateReference;
	@JsonProperty("batch_collection_status_code")
	public List<String> batchCollectionStatusCode;
	@JsonProperty("transaction_type")
	public List<String> transactionType;
	@JsonProperty("collection_status")
	public List<String> collectionStatus;
	@JsonProperty("collection_notes")
	public List<String> collectionNotes;
	@JsonProperty("collection_amount")
	public List<String> collectionAmount;
	@JsonProperty("cc_authorisation_code")
	public List<String> ccAuthorisationCode;
	@JsonProperty("max_amount")
	public List<String> maxAmount;
	@JsonProperty("cc_transaction_id")
	public List<String> ccTransactionId;
	@JsonProperty("batch_collection_date")
	public List<String> batchCollectionDate;
	@JsonProperty("transaction_reference")
	public List<String> transactionReference;
	@JsonProperty("collection_description")
	public List<String> collectionDescription;
	@JsonProperty("collection_reference")
	public List<String> collectionReference;
	@JsonProperty("collection_status_code")
	public List<String> collectionStatusCode;
	@JsonProperty("batch_collection_event")
	public List<String> batchCollectionEvent;

}
