package com.example.gymdash.controllers.auth;

import com.example.gymdash.dtos.auth.AuthResponse;
import com.example.gymdash.dtos.auth.LoginRequest;
import com.example.gymdash.dtos.auth.RegisterRequest;
import com.example.gymdash.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
}
