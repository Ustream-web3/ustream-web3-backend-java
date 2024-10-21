package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.PasswordResetDTO;

public interface PasswordResetService {
    void requestPasswordReset(String email);
    void resetPassword(String token,  PasswordResetDTO passwordResetDto);


}
