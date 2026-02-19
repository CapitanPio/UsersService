package com.auth.users_service.dto;
import com.auth.users_service.validation.ValidPassword;


@lombok.Data
@lombok.NoArgsConstructor
public class ChangePasswordRequest {
    
    private String oldPassword;
    @ValidPassword
    private String newPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
