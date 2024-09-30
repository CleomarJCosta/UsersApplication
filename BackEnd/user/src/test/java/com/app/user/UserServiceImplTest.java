package com.app.user;


import com.app.user.Entity.User;
import com.app.user.Exception.EmailAlreadyExistsException;
import com.app.user.Repository.UserRepository;
import com.app.user.Service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals(user.getName(), registeredUser.getName());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class, () -> userService.register(user)
        );

        assertEquals("The email is already in use: test@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        boolean isAuthenticated = userService.authenticate("test@example.com", "password");

        assertTrue(isAuthenticated);
    }

    @Test
    void authenticate_Failure() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        boolean isAuthenticated = userService.authenticate("test@example.com", "wrongPassword");

        assertFalse(isAuthenticated);
    }

    @Test
    void findByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }
}

