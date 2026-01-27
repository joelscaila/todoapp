package com.joel.todoapp.service;

import com.joel.todoapp.model.User;
import com.joel.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("joel");
    }

    @Test
    void getCurrentUser_shouldReturnUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("joel");
        when(userRepository.findByUsername("joel")).thenReturn(Optional.of(user));

        SecurityContextHolder.setContext(securityContext);

        User result = userService.getCurrentUser();

        assertEquals("joel", result.getUsername());
        verify(userRepository).findByUsername("joel");
    }

    @Test
    void getCurrentUser_shouldThrowWhenUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        SecurityContextHolder.setContext(securityContext);

        assertThrows(RuntimeException.class, () ->
                userService.getCurrentUser()
        );
    }
}
