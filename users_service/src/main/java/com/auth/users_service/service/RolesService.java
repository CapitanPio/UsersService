package com.auth.users_service.service;
import java.util.List;

import com.auth.users_service.dto.CreateRoleRequest;
import com.auth.users_service.model.Permission;
import com.auth.users_service.model.Role;
import com.auth.users_service.repository.PermissionRepository;
import com.auth.users_service.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.auth.users_service.dto.EditRoleRequest;

@Service
@lombok.RequiredArgsConstructor
public class RolesService {


    private final RoleRepository roleRepository;
    private final PermissionRepository permissionsRepository;


    public Role createRole(CreateRoleRequest request) {

        List<Permission> permissions = new ArrayList<>(permissionsRepository.findAll().stream()
                .filter(permission -> request.getPermissions().contains(permission.getName()))
                .toList());

        if (permissions.size() != request.getPermissions().size()) {
            if (request.isCreateMissingPermissions()) {
                List<String> missingPermissions = request.getPermissions().stream()
                        .filter(name -> permissions.stream().noneMatch(p -> p.getName().equals(name)))
                        .toList();
                for (String missingPermission : missingPermissions) {
                    Permission newPermission = new Permission(missingPermission);
                    permissionsRepository.save(newPermission);
                    permissions.add(newPermission);
                }
            } else {
                throw new RuntimeException("One or more permissions not found");
            }
        }
        Role role = new Role(request.getName());
        role.getPermissions().addAll(permissions);
        roleRepository.save(role);
        return role;
    }

    public Role readRole(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
        return role;
    }

    public List<Role> readAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    public void deleteRole(String name) {
        roleRepository.deleteByName(name);
        return;
    }

    public Role editRole(EditRoleRequest request) {
        Role role = roleRepository.findByName(request.getName()).orElseThrow(() -> new RuntimeException("Role not found"));
        if (request.getNewName() != null) {
            role.setName(request.getNewName());
        }
        if (request.getPermissionsToAdd() != null) {
            List<Permission> permissionsToAdd = new ArrayList<>(permissionsRepository.findAll().stream()
                    .filter(permission -> request.getPermissionsToAdd().contains(permission.getName()))
                    .toList());

            if (permissionsToAdd.size() != request.getPermissionsToAdd().size()) {
                if (request.isCreateMissingPermissions()) {
                    List<String> missingPermissions = request.getPermissionsToAdd().stream()
                            .filter(name -> permissionsToAdd.stream().noneMatch(p -> p.getName().equals(name)))
                            .toList();
                    for (String missingPermission : missingPermissions) {
                        Permission newPermission = new Permission(missingPermission);
                        permissionsRepository.save(newPermission);
                        permissionsToAdd.add(newPermission);
                    }
                } else {
                    throw new RuntimeException("One or more permissions not found");
                }
            }
            role.getPermissions().addAll(permissionsToAdd);
        }
        if (request.getPermissionsToRemove() != null) {
            List<Permission> permissionsToRemove = role.getPermissions().stream()
                    .filter(permission -> request.getPermissionsToRemove().contains(permission.getName()))
                    .toList();
            role.getPermissions().removeAll(permissionsToRemove);
        }
        roleRepository.save(role);
        return role;
    }
}
