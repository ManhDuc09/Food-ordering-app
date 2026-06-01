package com.example.backend.service;

import com.example.backend.dto.auth.AuthRequest;
import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.jwt.JwtService;
import com.example.backend.model.Branch;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_validCredentials_returnsTokenAndUserInfo() {
        AuthRequest request = authRequest("user@example.com", "password");

        User user = new User();
        user.setEmail("user@example.com");
        user.setRole("customer");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("user@example.com");
        assertThat(response.getRole()).isEqualTo("customer");
        assertThat(response.getBranchName()).isNull();
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_userWithBranch_returnsBranchName() {
        AuthRequest request = authRequest("staff@example.com", "password");

        Branch branch = new Branch();
        branch.setName("District 1");

        User user = new User();
        user.setEmail("staff@example.com");
        user.setRole("staff");
        user.setBranch(branch);

        when(userRepository.findByEmail("staff@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getBranchName()).isEqualTo("District 1");
    }

    @Test
    void login_badCredentials_throwsException() {
        AuthRequest request = authRequest("user@example.com", "wrong");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void register_newUser_savesUserAndReturnsToken() {
        AuthRequest request = authRequest("new@example.com", "password123");
        request.setFullName("John Doe");
        request.setPhoneNumber("0123456789");

        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("new@example.com");
        assertThat(response.getRole()).isEqualTo("customer");
        assertThat(response.getBranchName()).isNull();
        verify(userRepository).save(any(User.class));
    }

    private AuthRequest authRequest(String email, String password) {
        AuthRequest r = new AuthRequest();
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }
}
