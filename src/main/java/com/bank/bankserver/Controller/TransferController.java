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

import com.bank.bankserver.Services.TransferService;
import com.bank.bankserver.DTOs.TransferDTO;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public TransferDTO transfer(@RequestBody Map<String, String> body) {
        return transferService.transfer(
                Long.parseLong(body.get("fromId")),
                body.get("toNumber"),
                new BigDecimal(body.get("amount")));
    }

    @GetMapping("/{accountId}")
    public List<TransferDTO> history(@PathVariable Long accountId) {
        return transferService.getHistory(accountId);
    }
}
