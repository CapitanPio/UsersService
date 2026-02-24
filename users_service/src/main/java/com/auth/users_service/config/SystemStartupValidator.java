package com.auth.users_service.config;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import com.auth.users_service.repository.PermissionRepository;
import com.auth.users_service.repository.RoleRepository;
import com.auth.users_service.model.Role;
import org.springframework.stereotype.Component;


@Component
public class SystemStartupValidator implements ApplicationListener<ApplicationReadyEvent> {

    private final RolesProperties rolesProperties;
    private final PermissionsProperties permissionsProperties;

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionsRepository;


    public SystemStartupValidator(PermissionsProperties permissionsProperties, 
                                    RolesProperties rolesProperties, 
                                    RoleRepository roleRepository, 
                                    PermissionRepository permissionsRepository) {
        this.permissionsProperties = permissionsProperties;
        this.rolesProperties = rolesProperties;
        this.roleRepository = roleRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        if (rolesProperties.getAllowed()) {
            // if roles are allowed, we need to ensure that the base role exists
            roleRepository.save(new Role(rolesProperties.getBaseRole()));
        }
    }
}