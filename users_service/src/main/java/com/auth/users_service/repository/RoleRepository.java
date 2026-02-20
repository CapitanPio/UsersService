package com.auth.users_service.repository;

import com.auth.users_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
    List<Role> findAll();
}