package com.parking.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import com.parking.auth.auth.AuthResponse;
import com.parking.auth.auth.AuthService;
import com.parking.auth.auth.LoginRequest;
import com.parking.auth.auth.RegisterRequest;
import com.parking.auth.auth.UserResponse;
import com.parking.auth.role.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ParkingAuthServiceApplicationTests {

    @Autowired
    private AuthService authService;

    @Test
    void contextLoads() {
    }

    @Test
    void registerAndLoginReturnsBearerToken() {
        UserResponse user = authService.register(new RegisterRequest(
                "adminuser",
                "admin@example.com",
                "password123",
                Set.of(RoleName.ADMIN, RoleName.USER)
        ));

        AuthResponse response = authService.login(new LoginRequest("adminuser", "password123"));

        assertThat(user.roles()).contains(RoleName.ADMIN, RoleName.USER);
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.roles()).contains(RoleName.ADMIN, RoleName.USER);
    }
}