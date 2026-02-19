package com.auth.users_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;



@Entity
@lombok.Data
@lombok.NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String email;
    private String password;
    private Integer tokenVersion;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.tokenVersion = 0;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implement in the future
        return null;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public UserDetails getUserDetails() {
        return this;
    }
}
