package com.bank.bankserver.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bankserver.entity.Account;
import com.bank.bankserver.entity.Transfer;
import com.bank.bankserver.repository.AccountRepository;
import com.bank.bankserver.repository.TransferRepository;
import com.bank.bankserver.DTOs.TransferDTO;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    public TransferDTO transfer(long fromId, String toNumber, BigDecimal amount) {
        Account fromAcc = accountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account toAcc = accountRepository.findByAccountNumber(toNumber);
        if (toAcc == null)
            throw new RuntimeException("Receiver not found");
        if (fromAcc.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
        toAcc.setBalance(toAcc.getBalance().add(amount));

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        Transfer t = new Transfer();
        t.setFromAccount(fromAcc);
        t.setToAccount(toAcc);
        t.setAmount(amount);
        t.setDescription("Transfer");

        Transfer saved = transferRepository.save(t);
        return toDTO(saved);
    }

    public List<TransferDTO> getHistory(long accountId) {
        return transferRepository.findByFromAccountAccountIdOrToAccountAccountId(accountId, accountId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransferDTO toDTO(Transfer t) {
        TransferDTO dto = new TransferDTO();
        dto.setTransferId(t.getTransferId());
        dto.setFromAccountId(t.getFromAccount().getAccountId());
        dto.setToAccountId(t.getToAccount().getAccountId());
        dto.setAmount(t.getAmount());
        dto.setTransferDate(t.getTransferDate());
        dto.setDescription(t.getDescription());
        return dto;
    }
}
