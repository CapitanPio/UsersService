package com.auth.users_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.auth.users_service.model.User;
import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    void deleteByUsername(String username);
}
