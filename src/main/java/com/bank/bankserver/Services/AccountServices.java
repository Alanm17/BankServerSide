package com.bank.bankserver.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.bank.bankserver.DTOs.AccountDTO;
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

    public AccountDTO createAccount(long userId, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account acc = new Account();
        acc.setUser(user);
        acc.setAccountType(Account.AccountType.valueOf(type));
        acc.setAccountNumber(UUID.randomUUID().toString().substring(0, 10));
        acc.setBalance(BigDecimal.ZERO);

        Account saved = accountRepository.save(acc);

        return toDTO(saved);
    }

    public List<AccountDTO> getUserAccounts(long userId) {
        return accountRepository.findByUserUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO getByNumber(String number) {
        Account acc = accountRepository.findByAccountNumber(number);
        return toDTO(acc);
    }

    private AccountDTO toDTO(Account acc) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(acc.getAccountId());
        dto.setAccountNumber(acc.getAccountNumber());
        dto.setAccountType(acc.getAccountType().name());
        dto.setBalance(acc.getBalance());
        return dto;
    }
}
