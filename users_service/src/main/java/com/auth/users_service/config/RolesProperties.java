package com.auth.users_service.config;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "roles")
@lombok.Data
public class RolesProperties {

    private Boolean allowed;
    private String baseRole;

    // Users without roles (ignored if you allow roles)
    private String baseUsername;
}
