package com.auth.users_service.repository;

import com.auth.users_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
    List<Role> findAll();

    Optional<Role> findByName(String name);

    @Transactional
    void deleteByName(String name);
}