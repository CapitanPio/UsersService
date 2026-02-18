package com.auth.authentication_service.dto;

@lombok.Data
@lombok.NoArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;

    public UserRegistrationRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
    