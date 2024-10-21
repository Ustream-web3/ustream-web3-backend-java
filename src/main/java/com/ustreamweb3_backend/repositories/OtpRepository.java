package com.ustreamweb3_backend.repositories;

import com.ustreamweb3_backend.dtos.OtpVerificationDTO;
import com.ustreamweb3_backend.entities.Otp;
import com.ustreamweb3_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Otp findByUserAndOtpCode(OtpVerificationDTO otpVerificationDTO, User user);
    // Find an OTP by its code and associated user
    Optional<Otp> findByOtpCodeAndUser(String otpCode, User user);
}
