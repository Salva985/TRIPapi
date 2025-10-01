package com.tripapi.security;

import com.tripapi.model.User;
import com.tripapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
public class CurrentUser {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public CurrentUser(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /** Returns the current user or throws 401/403 if missing/invalid. */
    public User require() {
        return get().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing/invalid token"));
    }

    /** Returns Optional<User> if you want to handle anon differently. */
    public Optional<User> get() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return Optional.empty();

        String token = auth.substring("Bearer ".length()).trim();
        String email;
        try {
            email = jwtUtil.extractSubject(token); // subject == email in your JwtUtil
        } catch (Exception e) {
            return Optional.empty();
        }
        return userRepository.findByEmailIgnoreCase(email);
    }
}