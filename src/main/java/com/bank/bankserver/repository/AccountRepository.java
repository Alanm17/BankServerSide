package com.bank.bankserver.repository;

import com.bank.bankserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserId(Long userId);

    Account findByAccountNumber(String accountNumber);
}
