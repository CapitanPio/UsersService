package com.auth.users_service.service;
import com.auth.users_service.config.PermissionsProperties;
import com.auth.users_service.config.RolesProperties;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class CheckAccessService {
    private final PermissionsProperties permissionsProperties;
    private final RolesProperties rolesProperties;


    
    public CheckAccessService(PermissionsProperties permissionsProperties, RolesProperties rolesProperties) {
        this.permissionsProperties = permissionsProperties;
        this.rolesProperties = rolesProperties;
    }

    public boolean checkAccess(OperationType operationType, String entityType) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getRoles();
        List<String> permissionNames = SecurityContextHolder.getContext().getAuthentication().getPermissions();

        if (!rolesProperties.getAllowed() && !permissionsProperties.getAllowed()) {
            return internalCheckAccess(username, operationType);
        }
        else if (rolesProperties.getAllowed() && !permissionsProperties.getAllowed()) {
            return internalCheckAccess(username, role, permissionNames, operationType, entityType);
        }
        else if (permissionsProperties.getAllowed()) {
            return internalCheckAccess(permissionNames, operationType, entityType);
        }
        else{
            return false;
        }
    }

    // No roles nor permissions allowed
    private boolean internalCheckAccess(String username, OperationType operationType) {
        if (username.equals(rolesProperties.getBaseUsername())) {
            return true;
        }
        if (!permissionsProperties.getUsersCrudReservedToBaseRole().contains(operationType.name())) {
            return true;
        }
        return false;
    }

    // No permissions allowed
    private boolean internalCheckAccess(String username, String role, List<String> permissionNames, OperationType operationType, String entityType) {
        if (role.equals(rolesProperties.getBaseRole())) {
            return true;
        }
        if (entityType.equals("USER") && !permissionsProperties.getUsersCrudReservedToBaseRole().contains(operationType.name())) {
            return true;
        }
        else if (entityType.equals("ROLE") && !permissionsProperties.getRolesCrudReservedToBaseRole().contains(operationType.name())) {
            return true;
        }
        else {
            return false;
        }
    }

    // Permissions allowed
    private boolean internalCheckAccess(List<String> permissionNames, OperationType operationType, String entityType) {

        if (entityType.equals("USER")) {
            if (operationType == OperationType.R && permissionNames.contains("CAN_READ_USERS")) {
                return true;
            }
            else if (operationType == OperationType.C && permissionNames.contains("CAN_CREATE_USERS")) {
                return true;
            }
            else if (operationType == OperationType.U && permissionNames.contains("CAN_EDIT_USERS")) {
                return true;
            }
            else if (operationType == OperationType.D && permissionNames.contains("CAN_DELETE_USERS")) {
                return true;
            }
            else{
                return false;
            }
            
        }

        else if (entityType.equals("ROLE")) {
            if (operationType == OperationType.R && permissionNames.contains("CAN_READ_ROLES")) {
                return true;
            }
            else if (operationType == OperationType.C && permissionNames.contains("CAN_CREATE_ROLES")) {
                return true;
            }
            else if (operationType == OperationType.U && permissionNames.contains("CAN_EDIT_ROLES")) {
                return true;
            }
            else if (operationType == OperationType.D && permissionNames.contains("CAN_DELETE_ROLES")) {
                return true;
            }

            return false;
        }

        else if (entityType.equals("PERMISSION")) {
            if (operationType == OperationType.R && permissionNames.contains("CAN_READ_PERMISSIONS")) {
                return true;
            }
            else if (operationType == OperationType.C && permissionNames.contains("CAN_CREATE_PERMISSIONS")) {
                return true;
            }
            else if (operationType == OperationType.U && permissionNames.contains("CAN_EDIT_PERMISSIONS")) {
                return true;
            }
            else if (operationType == OperationType.D && permissionNames.contains("CAN_DELETE_PERMISSIONS")) {
                return true;
            }

            return false;
        }
        else{
            return false;
        }
    }
}