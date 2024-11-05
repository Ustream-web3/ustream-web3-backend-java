package com.ustreamweb3_backend.config;


import com.ustreamweb3_backend.services.CustomUsersDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUsersDetailsService customUsersDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUsersDetailsService customUsersDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUsersDetailsService = customUsersDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session management
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/videos/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/videos/filter/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/videos/search/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/leaderboard/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/verify-otp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/resend-otp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/password-reset/request/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/password-reset/reset/**").permitAll()// Public verify-otp endpoint
                        .requestMatchers(HttpMethod.GET, "/authentication-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/videos/stream/**").authenticated() // Public docs endpoint
                        .anyRequest().authenticated())  // Private endpoints
                .authenticationManager(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // Add the JWT filter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
