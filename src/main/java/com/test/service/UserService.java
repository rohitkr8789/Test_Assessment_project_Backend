package com.test.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.dto.LoginRequest;
import com.test.dto.LoginResponse;
import com.test.dto.RegisterRequest;
import com.test.entity.Role;
import com.test.entity.User;
import com.test.repository.UserRepository;
import com.test.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        String imageUrl = cloudinaryService.uploadImage(request.getImage());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CANDIDATE)
                .imageUrl(imageUrl)
                .dateOfBirth(request.getDateOfBirth())   
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return "Registration successful";
    }
    
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getName(),
                user.getEmail(),
                user.getImageUrl(),
                user.getDateOfBirth()
        );
    }
    
    public boolean verifyUser(String email, LocalDate dob) {

        Optional<User> user = userRepository.findByEmailAndDateOfBirth(email, dob);

        return user.isPresent();
    }
    
    public String resetPassword(String email, String newPassword) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            // encrypt new password
            user.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(user);

            return "Password reset successful";
        }

        return "User not found";
    }
}
