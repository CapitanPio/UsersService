package com.auth.authentication_service.dto;

@lombok.Data
@lombok.NoArgsConstructor
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
