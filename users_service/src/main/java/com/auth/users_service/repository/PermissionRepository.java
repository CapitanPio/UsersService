package com.auth.users_service.repository;

import com.auth.users_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);
    List<Permission> findAll();
}