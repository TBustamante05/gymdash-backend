package com.example.gymdash.dtos.auth;

public record LoginRequest(
    String username,
    String password
) {}
