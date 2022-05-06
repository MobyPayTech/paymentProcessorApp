package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.SaveCardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveCardDataEntityRepository extends JpaRepository<SaveCardData, Integer > {

    List<SaveCardData> findByCustIdAndCardRef(String custId, String cardRef);

    Optional<SaveCardData> findByCardRef( String cardRef);
    List<SaveCardData> findByCustId( String custId);


}
