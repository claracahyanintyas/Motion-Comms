package org.individualproject.semester3project.infrastructure.security.controller;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.security.dto.JwtAuthResponse;
import org.individualproject.semester3project.infrastructure.security.dto.LoginRequest;
import org.individualproject.semester3project.usecase.security.AuthService;
import org.individualproject.semester3project.usecase.security.dto.JwtAuthOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtAuthOutput jwtAuthOutput = authService.login(loginRequest);
        JwtAuthResponse jwtAuthResponse = JwtAuthResponse.builder()
                .accessToken(jwtAuthOutput.getAccessToken())
                .tokenType(jwtAuthOutput.getTokenType())
                .user(jwtAuthOutput.getUser())
                .build();
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }

}