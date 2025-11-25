package com.bank.bankserver.Controller;

import java.math.BigDecimal;
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

import com.bank.bankserver.Services.TransactionService;
import com.bank.bankserver.entity.Transaction;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public Transaction deposit(@RequestBody Map<String, String> body) {
        return transactionService.deposit(
                Long.parseLong(body.get("accountId")),
                new BigDecimal(body.get("amount"))
        );
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestBody Map<String, String> body) {
        return transactionService.withdraw(
                Long.parseLong(body.get("accountId")),
                new BigDecimal(body.get("amount"))
        );
    }

    @GetMapping("/{accountId}")
    public List<Transaction> history(@PathVariable Long accountId) {
        return transactionService.history(accountId);
    }
}
