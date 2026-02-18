package com.auth.authentication_service.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth.authentication_service.dto.UserRegistrationRequest;
import com.auth.authentication_service.dto.UserRegistrationResponse;
import com.auth.authentication_service.dto.AuthResponse;
import com.auth.authentication_service.dto.LoginRequest;
import com.auth.authentication_service.dto.ChangePasswordRequest;
import com.auth.authentication_service.dto.ChangePasswordResponse;
import com.auth.authentication_service.service.AuthService;
import com.auth.authentication_service.service.UsersManagerService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersManagerService usersManagerService;

    public AuthController(AuthService authService, UsersManagerService usersManagerService) {
        this.authService = authService;
        this.usersManagerService = usersManagerService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("Health endpoint hitted");
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest request) {
        
        System.out.println("Register endpoint hitted with username: " + request.getUsername());

        try{
            UserRegistrationResponse response = usersManagerService.register(request);
            return ResponseEntity.ok("User registered successfully: " + response.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        System.out.println("Login endpoint hitted with username: " + request.getUsername());

        try {
            AuthResponse response = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("User logged in successfully. Token: " + response.getToken());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
    System.out.println("Get users endpoint hitted");
        try{
            return ResponseEntity.ok(usersManagerService.getAllUsers().toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
        
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            usersManagerService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {

        try {
            ChangePasswordResponse response = usersManagerService.changePassword(request.getOldPassword(), request.getNewPassword());

            return ResponseEntity.ok("Password changed successfully: "+ response.getToken());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    
}