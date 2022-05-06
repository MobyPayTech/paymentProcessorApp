package com.mobpay.Payment.Service;

import com.mobpay.Payment.Repository.SaveCardDataEntityRepository;
import com.mobpay.Payment.dao.PaymentRequest;
import com.mobpay.Payment.dao.SaveCardData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class AddCardService {

    @Autowired
    SaveCardDataEntityRepository saveCardDataEntityRepository;


    public boolean saveCard(PaymentRequest paymentRequest,String cardLabel,String cardStatus){

        SaveCardData saveCardData = SaveCardData
                .builder()
                .custId(paymentRequest.getCustId())
                .custName(paymentRequest.getCustomerName())
                .custEmail(paymentRequest.getLoginId())
                .custMobile(paymentRequest.getMobileNo())
                .cardRef(paymentRequest.getCardRef())
                .cardNo(paymentRequest.getCarddetails().split("#")[0])
                .cardBrand(paymentRequest.getCardBrand())
                .cardLabel(cardLabel)
                .cardStatus(cardStatus)
                .createDate(new Date())
                .build();

        SaveCardData saveCardData1 = saveCardDataEntityRepository.save(saveCardData);
        if(Objects.nonNull(saveCardData1)){
            log.info("Successfully saved card details for customer "+paymentRequest.getCustId()+" for card reference "+paymentRequest.getCardRef());
            return true;
        }
        log.error("Error while saving card details for customer "+paymentRequest.getCustId()+" for card reference "+paymentRequest.getCardRef());

        return false;
    }

}
