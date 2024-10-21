package com.ustreamweb3_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationDTO {

    @NotBlank(message = "OTP is required")
    private String otp;


}
