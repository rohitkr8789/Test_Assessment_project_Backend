package com.test.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.*;

import com.test.dto.LoginRequest;
import com.test.dto.LoginResponse;
import com.test.dto.RegisterRequest;
import com.test.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
    
    @PostMapping("/verify-user")
    public boolean verifyUser(
            @RequestParam String email,
            @RequestParam String dob) {

        LocalDate dateOfBirth = LocalDate.parse(dob);
        return userService.verifyUser(email, dateOfBirth);
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {

        return userService.resetPassword(email, newPassword);
    }
}