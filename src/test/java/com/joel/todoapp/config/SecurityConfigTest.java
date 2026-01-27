package com.joel.todoapp.config;

import com.joel.todoapp.security.JwtAuthenticationFilter;
import com.joel.todoapp.security.JwtTokenService;
import com.joel.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpSecurity http;

    @Test
    void userDetailsService_shouldLoadUserDetailsBean() {
        SecurityConfig config = new SecurityConfig();

        UserDetailsService service = config.userDetailsService(userRepository);

        assertNotNull(service);
    }

    @Test
    void passwordEncoder_shouldReturnBCryptEncoder() {
        SecurityConfig config = new SecurityConfig();

        PasswordEncoder encoder = config.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void jwtAuthenticationFilter_shouldCreateFilter() {
        SecurityConfig config = new SecurityConfig();

        UserDetailsService uds = username -> null;

        JwtAuthenticationFilter filter = config.jwtAuthenticationFilter(jwtTokenService, uds);

        assertNotNull(filter);
    }

    @Test
    void authenticationManager_shouldReturnManager() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager())
                .thenReturn(authenticationManager);

        SecurityConfig config = new SecurityConfig();

        AuthenticationManager result = config.authenticationManager(authenticationConfiguration);

        assertNotNull(result);
    }
}
