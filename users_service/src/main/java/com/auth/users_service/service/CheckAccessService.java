package com.auth.users_service.service;
import com.auth.users_service.config.PermissionsProperties;
import com.auth.users_service.config.RolesProperties;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class CheckAccessService {
    private final PermissionsProperties permissionsProperties;
    private final RolesProperties rolesProperties;


    
    public CheckAccessService(PermissionsProperties permissionsProperties, RolesProperties rolesProperties) {
        this.permissionsProperties = permissionsProperties;
        this.rolesProperties = rolesProperties;
    }

    // This method checks if the currently authenticated user has the required permission or if is the admin user (if roles are not allowed)
    public boolean checkAccess(String requiredPermission) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (rolesProperties.getAllowed()) {
            return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(requiredPermission));
        }

        else {
            if (username.equals(rolesProperties.getBaseUsername())) {
                return true;
            }
        }
        return false;
    }
}