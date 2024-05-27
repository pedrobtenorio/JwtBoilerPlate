package com.pedrotenorio.jwtboilerplate.service;


import com.pedrotenorio.jwtboilerplate.domain.DTO.AuthResponse;
import com.pedrotenorio.jwtboilerplate.domain.DTO.LoginRequest;
import com.pedrotenorio.jwtboilerplate.domain.DTO.RegisterRequest;
import com.pedrotenorio.jwtboilerplate.domain.User;
import com.pedrotenorio.jwtboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .email(request.email())
                .createdDate(new Date())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userRepository.findByUsername(request.username());
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }
}
