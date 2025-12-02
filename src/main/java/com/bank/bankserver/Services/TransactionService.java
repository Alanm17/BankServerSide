package com.bank.bankserver.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bankserver.entity.Account;
import com.bank.bankserver.entity.Transaction;
import com.bank.bankserver.repository.AccountRepository;
import com.bank.bankserver.repository.TransactionRepository;
import com.bank.bankserver.DTOs.TransactionDTO;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    public TransactionDTO deposit(long accountId, BigDecimal amount) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        acc.setBalance(acc.getBalance().add(amount));
        accountRepository.save(acc);
        Transaction tx = new Transaction();
        tx.setAccount(acc);
        tx.setAmount(amount);
        tx.setType(Transaction.TransactionType.Deposit);
        tx.setDescription("Deposit");
        Transaction saved = transactionRepository.save(tx);
        return toDTO(saved);
    }

    public TransactionDTO withdraw(long accountId, BigDecimal amount) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (acc.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepository.save(acc);

        Transaction tx = new Transaction();
        tx.setAccount(acc);
        tx.setAmount(amount);
        tx.setType(Transaction.TransactionType.Withdrawal);
        tx.setDescription("Withdraw");
        Transaction saved = transactionRepository.save(tx);
        return toDTO(saved);
    }

    public List<TransactionDTO> history(long accountId) {
        return transactionRepository.findByAccountAccountId(accountId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO toDTO(Transaction tx) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(tx.getTransactionId());
        dto.setAccountId(tx.getAccount().getAccountId());
        dto.setType(tx.getType().name());
        dto.setAmount(tx.getAmount());
        dto.setTransactionDate(tx.getTransactionDate());
        dto.setDescription(tx.getDescription());
        return dto;
    }
}
