package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.CurlecChargeNowResultDao;
import com.mobpay.Payment.dao.SubsequentPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurlecChargeNowResultDaoEntityRepository extends JpaRepository<CurlecChargeNowResultDao, Integer > {
}
