package com.auth.users_service.config;

import java.security.Permissions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "permissions")
@lombok.Data
public class PermissionsProperties {

    private Boolean allowed;

    // Roles without permissions (ignored if you allow permissions)
    private String usersCrudReservedToBaseRole;
    private String rolesCrudReservedToBaseRole;

    // Users without roles nor permissions (ignored if you allow roles)
    private String usersCrudReservedToBaseUser;
    private String rolesCrudReservedToBaseUser;

}
