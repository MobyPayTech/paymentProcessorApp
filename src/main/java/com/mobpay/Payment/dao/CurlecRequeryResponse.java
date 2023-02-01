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
public class CurlecRequeryResponse {

	@JsonProperty("Status")
	public List<String> status;
	@JsonProperty("Response")
	public ArrayList<ArrayList<CollectionResponse>> response;
	@JsonProperty("Message")
	public List<String> message;
	@JsonProperty("Date")
	public List<String> date;
	@JsonProperty("Total")
	public List<Integer> total;
	
}
