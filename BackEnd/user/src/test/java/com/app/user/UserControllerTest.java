package com.app.user;

import com.app.user.Controller.UserController;
import com.app.user.Entity.User;
import com.app.user.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
        when(userService.register(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        when(userService.register(any(User.class)))
                .thenThrow(new com.app.user.Exception.EmailAlreadyExistsException("Email already in use"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already in use", response.getBody());
    }

    @Test
    void login_Success() {
        when(userService.authenticate("test@example.com", "password")).thenReturn(true);

        ResponseEntity<Map<String, String>> response = userController.login("test@example.com", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful!", response.getBody().get("message"));
    }

    @Test
    void login_InvalidCredentials() {
        when(userService.authenticate("test@example.com", "wrongPassword")).thenReturn(false);

        ResponseEntity<Map<String, String>> response = userController.login("test@example.com", "wrongPassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }
}

