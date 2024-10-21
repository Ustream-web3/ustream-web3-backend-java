package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.PasswordResetDTO;
import com.ustreamweb3_backend.entities.PasswordReset;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.repositories.PasswordResetRepository;
import com.ustreamweb3_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void requestPasswordReset(String email) {

        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a password reset token
        String token = UUID.randomUUID().toString();
        PasswordReset resetPassword = new PasswordReset();
        resetPassword.setUser(user);
        resetPassword.setToken(token);
        resetPassword.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour

        passwordResetRepository.save(resetPassword);

        // Send the email with the reset link
        emailService.sendPasswordResetEmail(user.getEmail(), token);

    }

    @Override
    public void resetPassword(String token, PasswordResetDTO passwordResetDto) {

        // Find the token in the repository
        PasswordReset resetToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // Check if the token is expired
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Check if passwords match
        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }


        // Find the user associated with the token
        User user = resetToken.getUser();

        // Encode the new password
        String encodedPassword = passwordEncoder.encode(passwordResetDto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user); // Save the updated user

        // Optionally, delete the token after use
        passwordResetRepository.delete(resetToken);
    }
}
