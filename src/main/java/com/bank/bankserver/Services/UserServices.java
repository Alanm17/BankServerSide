package com.bank.bankserver.Services;

import com.bank.bankserver.entity.User;
import com.bank.bankserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User found = userRepository.findByEmail(email);
        if (found == null)
            throw new RuntimeException("User not found");
        if (!found.getPassword().equals(password))
            throw new RuntimeException("Invalid password");
        return found;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    }
}
