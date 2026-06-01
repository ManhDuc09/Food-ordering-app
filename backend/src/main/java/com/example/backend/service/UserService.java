package com.example.backend.service;

import com.example.backend.dto.user.ChangePasswordRequest;
import com.example.backend.dto.user.UserProfileRequest;
import com.example.backend.dto.user.UserProfileResponse;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse getProfile() {
        User user = currentUser();
        return new UserProfileResponse(user.getFullName(), user.getEmail(), user.getPhoneNumber());
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        User user = currentUser();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        log.info("Profile updated for user: {}", user.getEmail());
        return new UserProfileResponse(user.getFullName(), user.getEmail(), user.getPhoneNumber());
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = currentUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.warn("Failed password change attempt for user: {}", user.getEmail());
            throw new RuntimeException("Mật khẩu hiện tại không đúng.");
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            throw new RuntimeException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getEmail());
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
