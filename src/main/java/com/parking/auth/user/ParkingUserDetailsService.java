package com.parking.auth.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ParkingUserDetailsService implements UserDetailsService {

    private final UserAccountRepository users;

    public ParkingUserDetailsService(UserAccountRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount user = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String[] roles = user.getRoles().stream()
                .map(Enum::name)
                .toArray(String[]::new);

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .disabled(!user.isEnabled())
                .build();
    }
}