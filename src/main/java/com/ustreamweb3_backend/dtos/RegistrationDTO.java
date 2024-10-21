package com.ustreamweb3_backend.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[a-zA-Z]+(\\s[a-zA-Z]+)+$", message = "Full name must contain only letters and spaces between first and last name")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 9, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[\\S]+$", message = "Username must not contain spaces")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}
