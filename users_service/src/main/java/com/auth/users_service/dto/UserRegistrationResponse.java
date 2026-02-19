package com.auth.users_service.dto;

public class UserRegistrationResponse {
    private String username;

    public UserRegistrationResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
