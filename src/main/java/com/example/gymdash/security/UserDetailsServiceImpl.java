package com.example.gymdash.security;

import com.example.gymdash.entities.User;
import com.example.gymdash.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Log temporal para diagnóstico
        System.out.println("Usuario cargado: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Role: " + user.getRole());

        return user;
    }

}
