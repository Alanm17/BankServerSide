package com.bank.bankserver.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bankserver.Services.AccountServices;
import com.bank.bankserver.entity.Account;


@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {
    @Autowired
    private AccountServices accountServices;

    @PostMapping("/create")
    public Account createAccount(@RequestBody Map<String, String> body) {
        return accountServices.createAccount(Long.parseLong(body.get("userId")), body.get("type"));
    }

    @GetMapping("/user/{userId}")
    public List<Account> userAccounts(@PathVariable Long userId) {
        return accountServices.getUserAccounts(userId);
    }

    @GetMapping("/number/{accNumber}")
    public Account getByNumber(@PathVariable String accNumber) {
        return accountServices.getByNumber(accNumber);
    }
}
