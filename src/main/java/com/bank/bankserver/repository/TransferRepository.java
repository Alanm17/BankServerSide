package com.bank.bankserver.repository;

import com.bank.bankserver.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByFromAccountAccountIdOrToAccountAccountId(Long fromAccountId, Long toAccountId);
}
