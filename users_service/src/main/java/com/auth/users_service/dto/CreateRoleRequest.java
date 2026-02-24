package com.auth.users_service.dto;
import java.util.List;


@lombok.Data
@lombok.NoArgsConstructor
public class CreateRoleRequest {

    private String name;
    private List<String> permissions;
    private boolean createMissingPermissions;

    public CreateRoleRequest(String name, List<String> permissions, boolean createMissingPermissions) {
        this.name = name;
        this.permissions = permissions;
        this.createMissingPermissions = createMissingPermissions;
    }
}
