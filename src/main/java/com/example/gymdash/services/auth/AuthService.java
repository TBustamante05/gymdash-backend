package com.example.gymdash.services.auth;

import com.example.gymdash.dtos.auth.AuthResponse;
import com.example.gymdash.dtos.auth.LoginRequest;
import com.example.gymdash.dtos.auth.RegisterRequest;
import com.example.gymdash.entities.Role;
import com.example.gymdash.entities.User;
import com.example.gymdash.exceptions.BadRequestException;
import com.example.gymdash.repositories.UserRepository;
import com.example.gymdash.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthResponse login (LoginRequest request) {
        // Pedir a Spring que autentique
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Cargar al usuario
        User user = userRepository.findByUsername(request.username())
                .orElseThrow();

        // Generar el token y mandar respuesta
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponse register(RegisterRequest request) {
        // Validar que no exista el username o email
        if (userRepository.existsByUsername(request.username()))
            throw new BadRequestException("El username ya está en uso");

        if (userRepository.existsByEmail(request.email()))
            throw new BadRequestException("El email ya está en uso");

        // Crear el usuario
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.MEMBER)
                .build();

        userRepository.save(user);

        // Generar el token y mandar respuesta
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
