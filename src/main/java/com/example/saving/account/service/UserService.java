package com.example.saving.account.service;

import com.example.saving.account.dto.RegistrationRequest;
import com.example.saving.account.model.User;
import com.example.saving.account.model.UserRole;
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
        if (userRepository.existsByEmail(request.email()) || userRepository.existsByCitizenId(request.citizenId())) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCitizenId(request.citizenId());
        user.setThaiName(request.thaiName());
        user.setEnglishName(request.englishName());
        user.setPinHash(passwordEncoder.encode(request.pin()));
        user.setRole(UserRole.CUSTOMER);
        return userRepository.save(user);
    }
}