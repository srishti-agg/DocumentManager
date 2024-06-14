package com.example.securedoc.security;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyOwnAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (UsernamePasswordAuthenticationToken) authentication;

        var userFromDb = userDetailsService.loadUserByUsername(user.getPrincipal().toString());

        if(user.getCredentials().equals(userFromDb.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(userFromDb,"[PASSWORD PROTECTED]", userFromDb.getAuthorities());
        }
        throw new BadCredentialsException("Unable to login");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //return true;
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
