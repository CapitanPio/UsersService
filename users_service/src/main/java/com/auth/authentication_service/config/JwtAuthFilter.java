package com.auth.authentication_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth.authentication_service.utils.JwtUtils;
import com.auth.authentication_service.model.User;


@Component
@lombok.RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService  userDetailsService ;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtils.isTokenValid(token)){
                String username = jwtUtils.extractUsername(token);
                User user = (User) userDetailsService.loadUserByUsername(username);

                if (jwtUtils.extractTokenVersion(token) != user.getTokenVersion()) {
                    filterChain.doFilter(request, response);
                    return;
                }


                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response); // Continue with the filter chain
    }
    
}
