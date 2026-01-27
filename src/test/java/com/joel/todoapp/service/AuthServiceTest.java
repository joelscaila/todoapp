package com.joel.todoapp.service;

import com.joel.todoapp.dto.LoginRequest;
import com.joel.todoapp.dto.RegisterRequest;
import com.joel.todoapp.model.Role;
import com.joel.todoapp.model.User;
import com.joel.todoapp.repository.UserRepository;
import com.joel.todoapp.security.CustomUserDetails;
import com.joel.todoapp.security.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("joel");
        user.setPassword("encodedPass");
        user.setRole(Role.ROLE_USER);

        userDetails = new CustomUserDetails(user);
    }

    @Test
    void login_shouldReturnJwtToken() {
        LoginRequest request = new LoginRequest("joel", "1234");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails)).thenReturn("jwt-token");

        String token = authService.login(request);

        assertEquals("jwt-token", token);
        verify(authenticationManager).authenticate(any());
        verify(jwtTokenService).generateToken(userDetails);
    }

    @Test
    void login_shouldThrowWhenCredentialsInvalid() {
        LoginRequest request = new LoginRequest("joel", "wrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(request)
        );
    }

    @Test
    void register_shouldCreateUserWhenUsernameNotExists() {
        RegisterRequest request = new RegisterRequest("joel", "joel@mail.com", "1234");

        when(userRepository.existsByUsername("joel")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded");

        authService.register(request);

        verify(userRepository).existsByUsername("joel");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest("joel", "mail@mail.com", "1234");

        when(userRepository.existsByUsername("joel")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }
}
