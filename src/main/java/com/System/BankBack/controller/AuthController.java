package com.System.BankBack.controller;

import com.System.BankBack.dto.AuthRequest;
import com.System.BankBack.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * POST /auth/login
     * Body JSON → { "username": "...", "password": "..." }
     * Devuelve un JWT en el campo <token>.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest request) {

        // 1. Autenticar credenciales
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        // 2. Generar JWT
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        // 3. Devolverlo al cliente
        return ResponseEntity.ok(new TokenResponse(token));
    }

    /* DTO para la respuesta (opcional, solo por claridad) */
    @Getter @AllArgsConstructor
    private static class TokenResponse {
        private final String token;
    }
}

