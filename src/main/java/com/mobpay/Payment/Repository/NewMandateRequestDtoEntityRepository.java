package com.mobpay.Payment.Repository;



import com.mobpay.Payment.dao.NewMandateRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewMandateRequestDtoEntityRepository extends JpaRepository<NewMandateRequestDto, Integer > {
}
