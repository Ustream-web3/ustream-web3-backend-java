package com.ustreamweb3_backend.config;

import com.ustreamweb3_backend.services.CustomUsersDetailsService;
import com.ustreamweb3_backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUsersDetailsService customUsersDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUsersDetailsService customUsersDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUsersDetailsService = customUsersDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }

        // If username is present and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(username);

            // Validate the token and set the authentication
            if (jwtUtils.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
