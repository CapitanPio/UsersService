package com.auth.authentication_service.service;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.auth.authentication_service.repository.UserRepository;
import com.auth.authentication_service.dto.UserRegistrationRequest;
import com.auth.authentication_service.dto.UserRegistrationResponse;
import com.auth.authentication_service.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import com.auth.authentication_service.dto.ChangePasswordResponse;
import com.auth.authentication_service.utils.JwtUtils;


@Service
@lombok.RequiredArgsConstructor
public class UsersManagerService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Value("${user.can-delete-own-account:false}")
    private boolean userCanDeleteOwnAccount;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String username) {

        if (!userCanDeleteOwnAccount) {  // if NOT allowed
            String acting_user = SecurityContextHolder.getContext().getAuthentication().getName();
            if (acting_user.equals(username)) {
                throw new RuntimeException("You can't delete your own account");
            }
        }
        userRepository.deleteByUsername(username);
    }

    public UserRegistrationResponse register(UserRegistrationRequest request) {
        // Here you would add logic to save the user to a database, hash the password, etc.
        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        User existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        User existingEmail = userRepository.findByEmail(request.getEmail());
        if (existingEmail != null) {
            throw new RuntimeException("Email already exists");
        }
        
        userRepository.save(user);  

        System.out.println("Registering user with username: " + request.getUsername());


        return new UserRegistrationResponse(user.getUsername());
    }

    public ChangePasswordResponse changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));

        user.setTokenVersion(user.getTokenVersion()+1);

        String token = jwtUtils.generateToken(user);

        userRepository.save(user);

        System.out.println("Password changed for user: " + username);
        return new ChangePasswordResponse(token);
    }
}
