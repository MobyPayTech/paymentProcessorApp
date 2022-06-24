package com.mobpay.Payment.Helper;

import com.mobpay.Payment.Repository.MerchantDaoEntityRepository;
import com.mobpay.Payment.Repository.SaveCardDataEntityRepository;
import com.mobpay.Payment.Repository.UsersDaoEntityRepository;
import com.mobpay.Payment.dao.MerchantsDao;
import com.mobpay.Payment.dao.SaveCardData;
import com.mobpay.Payment.dao.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentValidation {
	/*
    @Autowired
    MerchantDaoEntityRepository merchantDaoEntityRepository;

    @Autowired
    UsersDaoEntityRepository usersDaoEntityRepository;

    @Autowired
    SaveCardDataEntityRepository saveCardDataEntityRepository;

    public void validateRequestParamsForPayment(String loginId, String mobileNo, String merchantId, double amount) {
        validateLoginId(loginId);
        validateMobileNumber(mobileNo);
        validateMerchantId(merchantId);
        validateAmount(amount);
    }

    public void validateRequestParamsForSubsequentPayment( String mobileNo, String merchantId, double amount,String cardRef) {
        validateMobileNumber(mobileNo);
        // validateMerchantId(merchantId);   //TODO
        validateAmount(amount);
        // validateCardReference(cardRef);  //TODO
    }

    public void validateRequestParamsForRemoveCard( String custId, String mobileNo, String cardRef) {
        validateCustId(custId);
        validateMobileNumber(mobileNo);
        validateCardReference(cardRef);
    }

    public void validateLoginId(String loginId) {

        Optional<UsersDao> usersDao = usersDaoEntityRepository.findByEmail(loginId);

        if (usersDao.isEmpty()) {
            throw new ValidationException("login id doesn't exist");
        }

    }

    public void validateMobileNumber(String mobileNo) {
        Optional<UsersDao> usersDao = usersDaoEntityRepository.findByPhoneNo(mobileNo);

        if (usersDao.isEmpty()) {
            throw new ValidationException("Mobile number doesn't exist");
        }
    }

    public void validateMerchantId(String merchantId) {
        Optional<MerchantsDao> merchantsDao = merchantDaoEntityRepository.findByCode(merchantId);

        if (merchantsDao.isEmpty()) {
            throw new ValidationException("Merchant id mismatch. Merchant id not registered");
        }
    }

    public void validateAmount(double amount) {
       // Double amt = null;
      /*  try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            throw new ValidationException("Amount is invalid");
        }

        if (amount < 2) {
            throw new ValidationException("Amount should be greater than or equal to two");
        }

    }
/*
    public void validateCardReference(String cardRef) {
        Optional<SaveCardData> saveCardData = saveCardDataEntityRepository.findByCardRef(cardRef);

        if (saveCardData.isEmpty()) {
            throw new ValidationException("Card Reference doesn't exist");
        }
    }

    public void validateCustId(String custId) {
        List<SaveCardData> saveCardData = saveCardDataEntityRepository.findByCustId(custId);

        if (saveCardData.isEmpty()) {
            throw new ValidationException("Customer ID doesn't exist");
        }
    }*/
}

