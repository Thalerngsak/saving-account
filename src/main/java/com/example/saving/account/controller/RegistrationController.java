package com.example.saving.account.controller;

import com.example.saving.account.dto.RegistrationRequest;
import com.example.saving.account.model.User;
import com.example.saving.account.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok(user);
    }
}
