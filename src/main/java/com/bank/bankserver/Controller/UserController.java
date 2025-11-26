package com.bank.bankserver.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.bankserver.entity.User;
import com.bank.bankserver.Services.UserServices;
import com.bank.bankserver.DTOs.UserDTO;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        return userServices.register(user);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody Map<String, String> body) {
        return userServices.login(body.get("email"), body.get("password"));
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userServices.getUser(id);
    }
}
