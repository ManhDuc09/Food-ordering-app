package com.example.backend.controller;

import com.example.backend.dto.auth.AuthRequest;
import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse auth = authService.login(request);
        setJwtCookie(response, auth.getToken());
        return ResponseEntity.ok(publicResponse(auth));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse auth = authService.register(request);
        setJwtCookie(response, auth.getToken());
        return ResponseEntity.ok(publicResponse(auth));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        clearJwtCookie(response);
        return ResponseEntity.ok().build();
    }

    private Map<String, Object> publicResponse(AuthResponse auth) {
        return Map.of(
            "email",      auth.getEmail(),
            "role",       auth.getRole(),
            "branchName", auth.getBranchName() != null ? auth.getBranchName() : "",
            "expiresAt",  System.currentTimeMillis() + 86400000L
        );
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                // .secure(true)  // uncomment when deployed on HTTPS
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void clearJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
