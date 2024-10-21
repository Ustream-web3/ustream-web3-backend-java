package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.utils.JwtUtils;
import com.ustreamweb3_backend.dtos.LoginDTO;
import com.ustreamweb3_backend.dtos.RegistrationDTO;
import com.ustreamweb3_backend.entities.Role;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUsersDetailsService customUsersDetailsService;

    // A set to keep track of logged-out tokens
    private final Set<String> loggedOutTokens = ConcurrentHashMap.newKeySet();


    @Override
    public void registerUser(RegistrationDTO registrationDTO) {

        User user = User.builder()
                .fullName(registrationDTO.getFullName())
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        // Generate OTP and send to the user
        otpService.generateAndSaveOtp(user);

    }



    @Override
    public String loginUser(LoginDTO loginDTO) {

        // Retrieve the user from the database
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")); // Handle user not found

        // Check if the user is verified
        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified. Please complete OTP verification.");
        }

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        UserDetails userDetails = customUsersDetailsService.loadUserByUsername(loginDTO.getUsername());
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @Override
    public void logout() {

        // Invalidate the user's token
        String token = getCurrentToken();
        if (token != null) {
            loggedOutTokens.add(token); // Mark this token as logged out
        }
    }

    public boolean isTokenLoggedOut(String token) {
        return loggedOutTokens.contains(token);
    }

    private String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            return (String) authentication.getCredentials();
        }
        return null;
    }


}

