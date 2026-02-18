package com.auth.authentication_service.dto;

public class UserRegistrationResponse {
    private String username;

    public UserRegistrationResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
