
package com.auth.users_service.service;

import org.springframework.stereotype.Service;
import com.auth.users_service.repository.PermissionRepository;
import com.auth.users_service.model.Permission;
import java.util.List;
import jakarta.transaction.Transactional;


@Service
@lombok.RequiredArgsConstructor
public class PermissionsService {

    private final PermissionRepository permissionRepository;

    public String createPermission(String request) {
        if (permissionRepository.existsByName(request)) {
            throw new RuntimeException("Permission already exists");
        }
        Permission permission = new Permission(request);
        permissionRepository.save(permission);
        return permission.getName();
    }

    public String readPermission(String name) {
        Permission permission = permissionRepository.findByName(name);
        if (permission == null) {
            throw new RuntimeException("Permission not found");
        }
        return permission.getName();
    }

    public List<String> readAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(Permission::getName)
                .toList();
    }

    @Transactional
    public String deletePermission(String name) {
        Permission permission = permissionRepository.findByName(name);
        if (permission == null) {
            throw new RuntimeException("Permission not found");
        }
        permissionRepository.deleteByName(name);
        return name;
    }

    public String editPermission(String oldName, String newName) {
        Permission permission = permissionRepository.findByName(oldName);
        if (permission == null) {
            throw new RuntimeException("Permission not found");
        }
        if (permissionRepository.existsByName(newName)) {
            throw new RuntimeException("Permission with the new name already exists");
        }
        permission.setName(newName);
        permissionRepository.save(permission);
        return permission.getName();
    }
}