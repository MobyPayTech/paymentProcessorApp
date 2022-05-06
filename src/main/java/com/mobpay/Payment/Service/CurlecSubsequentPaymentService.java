package com.mobpay.Payment.Service;

import com.mobpay.Payment.Repository.CurlecChargeNowResultDaoEntityRepository;
import com.mobpay.Payment.Repository.CurlecSubsequentPaymentRequestEntityRepository;
import com.mobpay.Payment.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CurlecSubsequentPaymentService {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CurlecSubsequentPaymentRequestEntityRepository curlecSubsequentPaymentRequestEntityRepository;

    @Autowired
    CurlecChargeNowResultDaoEntityRepository curlecChargeNowResultDaoEntityRepository;

    public SubsequentPaymentResponse makeSubsequentPayment(SubsequentPaymentRequest subsequentPaymentRequest) {

        CurlecSubsequentPaymentRequest curlecSubsequentPaymentRequest = CurlecSubsequentPaymentRequest.builder()
                .merchantId("447300")
                .employeeId("44733")
                .collectionAmount(subsequentPaymentRequest.getAmount())
                .refNumber(subsequentPaymentRequest.getCardReference())
                .invoiceNumber(subsequentPaymentRequest.getBillCode())
                .build();
        curlecSubsequentPaymentRequestEntityRepository.save(curlecSubsequentPaymentRequest);

        String chargeNowUrl = "https://demo.curlec.com/curlec-services?method=60"
                + "&merchantId=" + curlecSubsequentPaymentRequest.getMerchantId()
                + "&employeeId=" + curlecSubsequentPaymentRequest.getEmployeeId()
                + "&collectionAmount=" + curlecSubsequentPaymentRequest.getCollectionAmount()
                + "&refNumber=" + curlecSubsequentPaymentRequest.getRefNumber()
                + "&invoiceNumber=" + curlecSubsequentPaymentRequest.getInvoiceNumber();


        log.info("Invoke charge now url :"+ chargeNowUrl);


        ResponseEntity<CurlecSubsequentSuccessBody> response = restTemplate.exchange(chargeNowUrl, HttpMethod.POST, null, CurlecSubsequentSuccessBody.class);

        log.info("Response of charge now :"+response.getBody()+" for charge now url"+chargeNowUrl);
        return parseToCurlecSubsequentResponse(response.getBody(),subsequentPaymentRequest);
    }

    private SubsequentPaymentResponse parseToCurlecSubsequentResponse(CurlecSubsequentSuccessBody curlecSubsequentSuccessBody,
                                                                      SubsequentPaymentRequest subsequentPaymentRequest) {


        String status = curlecSubsequentSuccessBody.getStatus().stream().findFirst().orElse("500");
        String dateTime = curlecSubsequentSuccessBody.getDate().get(0);
        SubsequentPaymentResponse subsequentPaymentResponse;


        if (status.equals("200")) {
            String transactionId = curlecSubsequentSuccessBody.getResponse().get(0).getCcTransactionId().get(0);
            String description = curlecSubsequentSuccessBody.getResponse().get(0).getCollectionStatus().get(0);

            subsequentPaymentResponse = SubsequentPaymentResponse.builder()
                    .responseCode("00")
                    .responseDescription(description)
                    .transactionId(transactionId)
                    .billCode(subsequentPaymentRequest.getBillCode())
                    .amount(subsequentPaymentRequest.getAmount())
                    .dateTime(dateTime)
                    .build();

            CurlecChargeNowResultDao curlecChargeNowResultDao = CurlecChargeNowResultDao.builder()
                    .cardReference(subsequentPaymentRequest.getBillCode())
                    .transactionId(transactionId)
                    .dateTime(dateTime)
                    .status(status)
                    .responsePayload(curlecSubsequentSuccessBody.toString())
                    .build();
            curlecChargeNowResultDaoEntityRepository.save(curlecChargeNowResultDao);
        } else {

             /*
                ToDo integrate Email notifier for error events
                 */

            String description = curlecSubsequentSuccessBody.getMessage().get(0);
            subsequentPaymentResponse = SubsequentPaymentResponse.builder()
                    .responseCode("01")
                    .responseDescription(description)
                    .dateTime(dateTime)
                    .build();

            CurlecChargeNowResultDao curlecChargeNowResultDao = CurlecChargeNowResultDao.builder()
                    .cardReference(subsequentPaymentRequest.getBillCode())
                    .transactionId(null)
                    .dateTime(dateTime)
                    .status(status)
                    .responsePayload(curlecSubsequentSuccessBody.toString())
                    .build();
            curlecChargeNowResultDaoEntityRepository.save(curlecChargeNowResultDao);
        }

        return subsequentPaymentResponse;

    }
}
