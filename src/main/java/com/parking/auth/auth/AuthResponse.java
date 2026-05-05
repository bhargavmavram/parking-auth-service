package com.parking.auth.auth;

import java.time.Instant;
import java.util.Set;

import com.parking.auth.role.RoleName;

public record AuthResponse(
        String tokenType,
        String accessToken,
        Instant expiresAt,
        String username,
        Set<RoleName> roles
) {
}