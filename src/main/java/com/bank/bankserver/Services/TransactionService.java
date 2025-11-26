package com.bank.bankserver.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.bank.bankserver.entity.Account;
import com.bank.bankserver.entity.Transaction;
import com.bank.bankserver.repository.AccountRepository;
import com.bank.bankserver.repository.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Transaction deposit(@NonNull Long accountId, BigDecimal amount) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        acc.setBalance(acc.getBalance().add(amount));
        accountRepository.save(acc);
        Transaction tx = new Transaction();
        tx.setAccount(acc);
        tx.setAmount(amount);
        tx.setType(Transaction.TransactionType.Deposit);
        tx.setDescription("Deposit");
        return transactionRepository.save(tx);
    }

    public Transaction withdraw(@NonNull Long accountId, BigDecimal amount) {
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

        return transactionRepository.save(tx);
    }

    public List<Transaction> history(Long accountId) {
        return transactionRepository.findByAccountAccountId(accountId);
    }
}
