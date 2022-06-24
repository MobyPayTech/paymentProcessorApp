package com.mobpay.Payment.Repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mobpay.Payment.dao.PaymentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pp_curlec_collectionStatusRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionStatusRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;
	@Column(name = "ccTransactionId")
	private String ccTransactionId;
	@Column(name = "clientType")
	private Integer clientType;
	private String merchantId;
	@Column(name = "CreatedAt")
	private Date createdAt;
	@Column(name = "UpdatedAt")
	private Date updatedAt;
}
