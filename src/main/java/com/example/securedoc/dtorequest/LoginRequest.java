package com.example.securedoc.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true)
public class LoginRequest {

    @NotEmpty(message = "First Name cannot be null/empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password cannot be null/empty")
    private String password;
}
