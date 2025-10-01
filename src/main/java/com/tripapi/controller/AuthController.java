package com.tripapi.controller;

import com.tripapi.dto.Auth.LoginRequest;
import com.tripapi.dto.Auth.RegisterRequest;
import com.tripapi.dto.Auth.AuthResponse;
import com.tripapi.dto.Auth.UserDTO;
import com.tripapi.model.User;
import com.tripapi.repository.UserRepository;
import com.tripapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // <-- inject bean (DO NOT call statically)

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        // you may want to check for duplicates (email/username) here

        User u = User.builder()
                .username(req.getUsername())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .dob(req.getDob() != null ? LocalDate.parse(req.getDob()) : null)
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();

        u = userRepository.save(u);

        String token = jwtUtil.generateToken(u.getEmail()); // <-- instance call
        AuthResponse body = new AuthResponse(token, UserDTO.fromEntity(u));

        return ResponseEntity.created(URI.create("/api/users/" + u.getId())).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User u = userRepository.findByEmailIgnoreCase(req.getEmail())
                .orElse(null);
        if (u == null || !passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getEmail()); // <-- instance call
        return ResponseEntity.ok(new AuthResponse(token, UserDTO.fromEntity(u)));
    }
}