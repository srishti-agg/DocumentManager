package com.example.securedoc.dto;

import com.example.securedoc.entity.RoleEntity;
import lombok.Data;

import static jakarta.persistence.FetchType.EAGER;

@Data
public class User {

    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer loginAttempts;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String phone;
    private String bio;
    private String imageUrl;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean mfa;
    private boolean enabled;
    private boolean credentialsNonExpired;
    private String qrCodeImageUri;
    private String role;
}
