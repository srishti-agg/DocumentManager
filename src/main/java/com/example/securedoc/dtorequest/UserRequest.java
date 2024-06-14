package com.example.securedoc.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true)
public class UserRequest {

    @NotEmpty(message = "First Name cannot be null/empty")
    private String firstName;
    @NotEmpty(message = "Last Name cannot be null/empty")
    private String lastName;
    @NotEmpty(message = "First Name cannot be null/empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password cannot be null/empty")
    private String password;
    private String bio;
    private String phone;

}
