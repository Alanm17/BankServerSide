package com.bank.bankserver.Services;

import com.bank.bankserver.entity.User;
import com.bank.bankserver.repository.UserRepository;
import com.bank.bankserver.DTOs.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("null")
    public UserDTO register(User user) { // Changed return type and removed @NonNull
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public UserDTO login(String email, String password) { // Changed return type
        User found = userRepository.findByEmail(email);
        if (found == null)
            throw new RuntimeException("User not found");
        if (!found.getPassword().equals(password))
            throw new RuntimeException("Invalid password");
        return toDTO(found); // Changed return
    }

    public UserDTO getUser(long userId) { // Changed return type and removed @NonNull
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")); // Added
                                                                                                               // local
                                                                                                               // variable
        return toDTO(user); // Changed return
    }

    // Added new private helper method
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
