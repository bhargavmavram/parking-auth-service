package com.parking.auth;

import java.time.Instant;
import java.util.Map;

import com.parking.auth.auth.AuthResponse;
import com.parking.auth.auth.AuthService;
import com.parking.auth.auth.LoginRequest;
import com.parking.auth.auth.RegisterRequest;
import com.parking.auth.auth.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Registration, login, and token issuing endpoints")
public class ParkingAuthController {

    private final AuthService authService;

    public ParkingAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth/status")
    @Operation(summary = "Get auth service status")
    public Map<String, String> status() {
        return Map.of(
                "service", "parking-auth-service",
                "message", "Parking Auth Service is running",
                "timestamp", Instant.now().toString()
        );
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Register a user and assign roles")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Login and receive a Bearer JWT")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}