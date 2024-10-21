package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.OtpVerificationDTO;
import com.ustreamweb3_backend.entities.Otp;
import com.ustreamweb3_backend.entities.User;





public interface OtpService {
    Otp generateAndSaveOtp(User user);
    boolean verifyOtp(OtpVerificationDTO otpVerificationDTO, User user);


}
