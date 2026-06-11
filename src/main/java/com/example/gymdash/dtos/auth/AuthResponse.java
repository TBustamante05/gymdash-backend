package com.example.gymdash.dtos.auth;

public record AuthResponse(
    String token,
    String username,
    String role
) {}
