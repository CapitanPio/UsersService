package com.auth.authentication_service.dto;

@lombok.Data
@lombok.NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
