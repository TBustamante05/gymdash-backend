package com.example.gymdash.dtos.auth;

public record RegisterRequest(
   String username,
   String email,
   String password
) {}
