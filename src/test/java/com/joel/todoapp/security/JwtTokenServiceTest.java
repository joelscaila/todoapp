package com.joel.todoapp.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;
    private UserDetails user;

    private static final String SECRET =
            "1234567890123456789012345678901234567890"; // >= 32 chars

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(SECRET, 3600000);

        user = User.withUsername("joel")
                .password("1234")
                .roles("USER")
                .build();
    }


    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtTokenService.generateToken(user);
        String username = jwtTokenService.extractUsername(token);

        assertEquals("joel", username);
    }

    @Test
    void extractUsername_shouldReturnCorrectValue() {
        String token = jwtTokenService.generateToken(user);
        assertEquals("joel", jwtTokenService.extractUsername(token));
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtTokenService.generateToken(user);


        assertTrue(jwtTokenService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {
        String token = jwtTokenService.generateToken(user);

        UserDetails user2 = User.withUsername("other")
                .password("1234")
                .roles("USER")
                .build();

        assertFalse(jwtTokenService.isTokenValid(token, user2));
    }
}

