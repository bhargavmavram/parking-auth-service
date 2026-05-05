package com.parking.auth.auth;

import java.util.Set;

import com.parking.auth.role.RoleName;

public record UserResponse(
        Long id,
        String username,
        String email,
        Set<RoleName> roles
) {
}