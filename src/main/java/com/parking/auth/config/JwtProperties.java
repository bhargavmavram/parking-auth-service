package com.parking.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "parking.jwt")
public record JwtProperties(String secret, String issuer, long expirationMinutes) {
}