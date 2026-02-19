package com.auth.users_service.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import com.auth.users_service.model.User;

import org.springframework.transaction.annotation.Transactional;


import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    @Transactional
    @Modifying
    void deleteByUsername(String username);

}