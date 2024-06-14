package com.example.securedoc.utils;

import com.example.securedoc.entity.RoleEntity;
import com.example.securedoc.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role) {

        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .loginAttempts(0)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .qrCodeSecret(EMPTY)
                .qrCodeImageUri(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .roles(role)
                .build();
    }
}
