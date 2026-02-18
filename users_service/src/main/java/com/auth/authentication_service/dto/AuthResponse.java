package com.auth.authentication_service.dto;

@lombok.Data
@lombok.NoArgsConstructor
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
