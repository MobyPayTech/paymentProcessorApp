package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.MerchantsDao;
import com.mobpay.Payment.dao.SubsequentPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantDaoEntityRepository extends JpaRepository<MerchantsDao, Integer > {

    Optional<MerchantsDao> findByCode(String code);
}
