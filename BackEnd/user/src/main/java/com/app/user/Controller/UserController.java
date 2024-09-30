package com.app.user.Controller;

import com.app.user.DTO.UserDTO;
import com.app.user.Entity.User;
import com.app.user.Exception.EmailAlreadyExistsException;
import com.app.user.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/users")
@RestController
public class UserController {
    private boolean loggedIn = false;

    @Autowired
    private UserService userService;

    @PostMapping
    public void post(@RequestBody User user){
        userService.addUser(user);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário está logado
        if (!loggedIn) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        // Aqui lista usuários se o usuário estiver autenticado
        List<UserDTO> users = userService.searchUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try{
            User createdUser = userService.register(user);
            return ResponseEntity.ok(createdUser);
        }catch (EmailAlreadyExistsException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Endpoint para login do usuário (autenticação)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(email, password);
        if (isAuthenticated) {
            loggedIn = true;
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            return ResponseEntity.ok(response);
        } else {
            loggedIn = false;
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }





}
