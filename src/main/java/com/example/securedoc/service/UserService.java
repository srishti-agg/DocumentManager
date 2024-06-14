package com.example.securedoc.service;

import com.example.securedoc.entity.RoleEntity;
import com.example.securedoc.enumeration.LoginType;

public interface UserService {

    void createUser(String firstName, String lastName, String email, String password);

    RoleEntity getRoleName(String name);

    void verifyAccountToken(String token);

    void updateLoginAttempt(String email, LoginType loginType);
}
