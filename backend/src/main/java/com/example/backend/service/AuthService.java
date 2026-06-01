package com.example.backend.service;

import com.example.backend.dto.auth.AuthRequest;
import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.jwt.JwtService;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;  

    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        log.info("Login successful for email: {}", user.getEmail());
        String token = jwtService.generateToken(user);
        String branchName = user.getBranch() != null ? user.getBranch().getName() : null;
        return new AuthResponse(token, user.getEmail(), user.getRole(), branchName);
    }

    public AuthResponse register(AuthRequest request) {
        log.info("Registering new user: {}", request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(UserRole.CUSTOMER.value);

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getEmail());
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getRole(), null);
    }
}