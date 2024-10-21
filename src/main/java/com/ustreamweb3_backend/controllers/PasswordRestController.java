package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.dtos.PasswordResetDTO;
import com.ustreamweb3_backend.services.PasswordResetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/password-reset")
public class PasswordRestController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset email sent." );
        } catch (RuntimeException e) {

            log.error("Error requesting password reset: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetDTO passwordResetDto) {
        log.info("Received password reset request for token: {}", token);

        try {
            passwordResetService.resetPassword(token, passwordResetDto);
            log.info("Password has been reset successfully for token: {}", token);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (RuntimeException e) {
            log.error("Error resetting password for token: {}. Error: {}", token, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while resetting password for token: {}. Error: {}", token, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }
}
