package com.mobpay.Payment.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pp_curlec_collectionStatusResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionStatusResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;
	@Column(name = "collection_status")
	private String collection_status;
	@Column(name = "cc_transaction_id")
	private String cc_transaction_id;
	@Column(name = "reference_number")
	private String reference_number;
	private String responseCode;
	private String responseMessage;
	@Column(name = "CreatedAt")
	private Date createdAt;
	@Column(name = "UpdatedAt")
	private Date updatedAt;

}
