package com.example.saving.account.controller;

import com.example.saving.account.dto.LoginRequest;
import com.example.saving.account.dto.TokenResponse;
import com.example.saving.account.model.UserRole;
import com.example.saving.account.repository.UserRepository;
import com.example.saving.account.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Customer Login")
    public ResponseEntity<TokenResponse> customerLogin(@RequestBody LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .filter(u -> u.getRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/teller/login")
    @Operation(summary = "Teller Login")
    public ResponseEntity<TokenResponse> tellerLogin(@RequestBody LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .filter(u -> u.getRole() == UserRole.TELLER)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
