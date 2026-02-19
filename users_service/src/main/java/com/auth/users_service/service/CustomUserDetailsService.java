package com.auth.users_service.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.users_service.repository.UserRepository;
import com.auth.users_service.model.User;

@Service
@lombok.RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        
        // find the user, throw if not found
        // return the User object (once it implements UserDetails)
        try {
            User user = userRepository.findByUsername(username);
            return user;
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
