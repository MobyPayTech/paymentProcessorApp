package com.mobpay.Payment.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionConflictResponse {

	private String status;
	private String message;
	private String date;
}
