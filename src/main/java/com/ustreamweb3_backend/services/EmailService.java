package com.ustreamweb3_backend.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendOtpEmail(String to, String otpCode);
    void sendRegistrationConfirmationEmail(String to);
    void sendPasswordResetEmail(String to, String token);
}
