package com.pedrotenorio.jwtboilerplate.controller;

import com.pedrotenorio.jwtboilerplate.domain.DTO.AuthResponse;
import com.pedrotenorio.jwtboilerplate.domain.DTO.LoginRequest;
import com.pedrotenorio.jwtboilerplate.domain.DTO.RegisterRequest;
import com.pedrotenorio.jwtboilerplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
