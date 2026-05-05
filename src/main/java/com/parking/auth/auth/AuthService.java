package com.parking.auth.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

import com.parking.auth.config.JwtProperties;
import com.parking.auth.role.RoleName;
import com.parking.auth.user.UserAccount;
import com.parking.auth.user.UserAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public AuthService(UserAccountRepository users, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                       JwtProperties jwtProperties) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already registered");
        }
        if (users.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        Set<RoleName> roles = request.roles() == null || request.roles().isEmpty()
                ? Set.of(RoleName.USER)
                : new LinkedHashSet<>(request.roles());

        UserAccount user = users.save(new UserAccount(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                roles
        ));

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserAccount user = users.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.expirationMinutes(), ChronoUnit.MINUTES);
        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new AuthResponse("Bearer", token, expiresAt, user.getUsername(), user.getRoles());
    }
}