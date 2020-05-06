package com.pazzio.raspberry.glove.repositories;

import com.pazzio.raspberry.glove.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findBySerialNumber(String serialNumber);

}
