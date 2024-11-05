package com.ustreamweb3_backend.services;


import com.ustreamweb3_backend.dtos.OtpVerificationDTO;
import com.ustreamweb3_backend.repositories.UserRepository;
import com.ustreamweb3_backend.utils.OtpUtils;
import com.ustreamweb3_backend.entities.Otp;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.repositories.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpUtils otpUtils;

    @Autowired
    private EmailService emailService;


    @Override
    public Otp generateAndSaveOtp(User user) {

        Otp otp = new Otp();

        // Use OtpUtils to generate OTP code and expiry date
        String otpCode = otpUtils.generateOtpCode();
        LocalDateTime expiryDate = otpUtils.generateOtpExpiryDate();

        // Set OTP properties
        otp.setUser(user);
        otp.setEmail(user.getEmail());
        otp.setOtpCode(otpCode);
        otp.setOtpExpiryDate(expiryDate);

        // Send OTP to the user
        emailService.sendOtpEmail(user.getEmail(), otpCode);

        // Save OTP in the database
        return otpRepository.save(otp);
    }

    @Override
    public boolean verifyOtp(OtpVerificationDTO otpVerificationDTO, User user) {

        // Check if user is null
        if (user == null) {
            return false;
        }

        // Find the OTP using the code from the DTO and the associated user
        Otp otp = otpRepository.findByOtpCodeAndUser(otpVerificationDTO.getOtp(), user).orElse(null);


        // Check if OTP is present and valid (not expired)
        if (otp != null && !otp.getOtpExpiryDate().isBefore(LocalDateTime.now())) {


            // Mark the user as verified
            user.setVerified(true);

            // Send OTP to the user
            emailService.sendRegistrationConfirmationEmail(user.getEmail());
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resendOtp(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a new OTP using OtpUtils
        String newOtpCode = otpUtils.generateOtpCode();
        LocalDateTime newExpiryDate = otpUtils.generateOtpExpiryDate();

        // Create or update the OTP entity
        Otp otp = otpRepository.findByUser(user).orElse(new Otp());
        otp.setOtpCode(newOtpCode);
        otp.setUser(user);
        otp.setOtpExpiryDate(newExpiryDate); // Set new expiry time
        otpRepository.save(otp);

        // Send the OTP to the user via email
        emailService.sendOtpEmail(user.getEmail(), newOtpCode);
    }




}
