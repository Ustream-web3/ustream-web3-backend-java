package com.ustreamweb3_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendOtpEmail(String to, String otpCode) {
        String subject = "Your OTP Code";
        String text = "Your OTP code is: " + otpCode;
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendRegistrationConfirmationEmail(String to) {
        String subject = "Registration Confirmation";
        String text = "Thank you for registering. Your account has been created successfully.";
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Password Reset Request";
        String text = "To reset your password, please click the following link: "
                + "http://yourdomain.com/reset-password?token=" + token;
        sendSimpleMessage(to, subject, text);
    }
}
