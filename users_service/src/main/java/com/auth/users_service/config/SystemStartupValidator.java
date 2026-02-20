

@Component
public class SystemStartupValidator implements ApplicationListener<ApplicationReadyEvent> {

    private final PermissionsProperties permissionsProperties;
    private final RolesProperties rolesProperties;
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
        if (permissionsProperties.getAllowed() && !rolesProperties.getAllowed()) {
            throw new IllegalStateException("Permissions cannot be allowed if roles are not allowed. Please check your configuration.");
        }
        if (rolesProperties.getAllowed()) {
            if (roleRepository.findAll().isEmpty()) {
                roleRepository.save(new Role(rolesProperties.getBaseRole()));
            }
            else if (!roleRepository.existsByName(rolesProperties.getBaseRole())) {
                roleRepository.save(new Role(rolesProperties.getBaseRole()));
            }
        }

        if (permissionsProperties.getAllowed()) {

            for(String permissionName : List.of(
                    "CAN_CREATE_USERS", "CAN_READ_USERS", "CAN_DELETE_USERS", "CAN_EDIT_USERS",
                    "CAN_CREATE_ROLES", "CAN_READ_ROLES", "CAN_DELETE_ROLES", "CAN_EDIT_ROLES",
                    "CAN_CREATE_PERMISSIONS", "CAN_READ_PERMISSIONS", "CAN_DELETE_PERMISSIONS", "CAN_EDIT_PERMISSIONS"
            )) {
                if (!permissionsRepository.existsByName(permissionName)) {
                    permissionsRepository.save(new Permission(permissionName));
                }
            }
        }
    }
}