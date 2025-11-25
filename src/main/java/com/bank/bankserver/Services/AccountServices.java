package com.bank.bankserver.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bankserver.entity.Account;
import com.bank.bankserver.entity.User;
import com.bank.bankserver.repository.AccountRepository;
import com.bank.bankserver.repository.UserRepository;

@Service
public class AccountServices {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Account createAccount(Long userId, String type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Account acc = new Account();
        acc.setUser(user);
        acc.setAccountType(Account.AccountType.valueOf(type));
        acc.setAccountNumber(UUID.randomUUID().toString().substring(0, 10));
        acc.setBalance(BigDecimal.ZERO);

        return accountRepository.save(acc);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserUserId(userId);
    }

    public Account getByNumber(String number) {
        return accountRepository.findByAccountNumber(number);
    }
}
