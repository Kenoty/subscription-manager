package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(max = 64)
    private String firstName;
    @NotBlank @Size(max = 64)
    private String lastName;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
}
