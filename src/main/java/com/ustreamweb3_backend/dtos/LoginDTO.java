package com.ustreamweb3_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String username;


    @NotBlank
    private String password;
}
