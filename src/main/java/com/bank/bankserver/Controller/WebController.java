package com.bank.bankserver.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/account/{id}")
    public String account(@PathVariable Long id, Model model) {
        model.addAttribute("accountId", id);
        return "account";
    }

    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }
}
