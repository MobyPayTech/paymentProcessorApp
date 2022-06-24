package com.mobpay.Payment.Repository;


import com.mobpay.Payment.dao.UsersDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersDaoEntityRepository { /* extends JpaRepository<UsersDao, Integer > {

    Optional<UsersDao> findByEmail(String email);
    Optional<UsersDao> findByPhoneNo(String phoneNo);
*/
}
