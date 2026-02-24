package com.auth.users_service.dto;

public class UserDTO {
    private String username;
    private String email;
    private Long id;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    
    public Long getId() {
        return id;
    }

}
