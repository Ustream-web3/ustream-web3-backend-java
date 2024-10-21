package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.LoginDTO;
import com.ustreamweb3_backend.dtos.RegistrationDTO;

public interface UserService {
    void registerUser(RegistrationDTO registrationDTO);

    String loginUser(LoginDTO loginDTO);

    void logout();
}
