package com.auth.authentication_service.dto;

public class ChangePasswordResponse {
    private String token;

    public ChangePasswordResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
