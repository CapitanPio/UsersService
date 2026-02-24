package com.auth.users_service.repository;

import com.auth.users_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import jakarta.transaction.Transactional;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);
    List<Permission> findAll();
    Permission findByName(String name);

    @Transactional
    void deleteByName(String name);

}