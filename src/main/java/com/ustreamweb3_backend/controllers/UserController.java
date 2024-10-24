package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.dtos.LoginDTO;
import com.ustreamweb3_backend.dtos.OtpVerificationDTO;
import com.ustreamweb3_backend.dtos.RegistrationDTO;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.repositories.UserRepository;
import com.ustreamweb3_backend.services.OtpService;
import com.ustreamweb3_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO) {
        System.out.println("Received Registration Data: " + registrationDTO);
        userService.registerUser(registrationDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout(); // Call the logout service method
        return ResponseEntity.ok("Successfully logged out.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        String token = userService.loginUser(loginDTO);
        return ResponseEntity.ok("Successfully logged in. Token: " + token);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDTO request,
                                            @RequestParam String username) {
        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElse(null); // Return null if the user is not found
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        boolean isVerified = otpService.verifyOtp(request, user);

        return isVerified
                ? ResponseEntity.ok("OTP verified successfully.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Return the list of users
    }


}
