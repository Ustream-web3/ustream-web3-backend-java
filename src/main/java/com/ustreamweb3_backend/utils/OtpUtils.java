package com.ustreamweb3_backend.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class OtpUtils {

    private static final int OTP_LENGTH = 6;

    // Generate a 6-digit numeric OTP
    public String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // ensures a 6-digit number
        return String.valueOf(otp);
    }

    // Method to set OTP expiry date (e.g., 5 minutes from now)
    public LocalDateTime generateOtpExpiryDate() {
        return LocalDateTime.now().plusMinutes(5);  // OTP valid for 5 minutes
    }

    // Method to check if the OTP is expired
    public boolean isOtpExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}