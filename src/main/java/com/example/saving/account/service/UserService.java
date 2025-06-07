package com.example.saving.account.service;

import com.example.saving.account.dto.RegistrationRequest;
import com.example.saving.account.model.User;
import com.example.saving.account.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByCitizenId(request.getCitizenId())) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCitizenId(request.getCitizenId());
        user.setThaiName(request.getThaiName());
        user.setEnglishName(request.getEnglishName());
        user.setPin(passwordEncoder.encode(request.getPin()));
        return userRepository.save(user);
    }
}
