package com.auth.authentication_service.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.auth.authentication_service.model.User;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {

        SecretKey key = getSigningKey();

        String token = Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key)
        .claim("token_version", user.getTokenVersion())
        .compact();

        return token;
    }

    public String validateToken(String token, String expected_username) {


        try {
            String username = extractUsername(token);

            if (!username.equals(expected_username)) {
                throw new RuntimeException("Invalid JWT token: username does not match");
            }

            return username;
        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
                throw new RuntimeException("JWT token has expired");
            } else {
                throw new RuntimeException("Invalid JWT token");
            }
        }
    }

    public boolean isTokenValid(String token) {

        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        
        SecretKey key = getSigningKey();

        try {
            String username = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            return username;
        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
                throw new RuntimeException("JWT token has expired");
            } else {
                throw new RuntimeException("Invalid JWT token");
            }
        } 
    }

    public int extractTokenVersion(String token) {
        SecretKey key = getSigningKey();

        try {
            Integer tokenVersion = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("token_version", Integer.class);

            return tokenVersion;
        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
                throw new RuntimeException("JWT token has expired");
            } else {
                throw new RuntimeException("Invalid JWT token");
            }
        } 
    }
}
    