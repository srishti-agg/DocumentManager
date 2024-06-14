package com.example.securedoc.controller;

import com.example.securedoc.domain.Response;
import com.example.securedoc.dtorequest.UserRequest;
import com.example.securedoc.entity.UserEntity;
import com.example.securedoc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/user"})
public class UserController {

    private final UserService userService;

    //private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user,
                                             HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());

        return ResponseEntity.created(URI.create("")).body(new Response(LocalDateTime.now().toString(),
                HttpStatus.CREATED.value(), request.getRequestURI(),
                HttpStatus.CREATED,
                "Account created. Check your email to enable account", EMPTY, emptyMap()));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("token") String token,
                                             HttpServletRequest request) {
        userService.verifyAccountToken(token);
        return ResponseEntity.ok().body(new Response(LocalDateTime.now().toString(),
                HttpStatus.OK.value(), request.getRequestURI(),
                HttpStatus.OK,
                "Account verified.", EMPTY, emptyMap()));
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
//
//        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
//                userRequest.getEmail(),userRequest.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(token);
//        return ResponseEntity.ok().body(Map.of("user",authenticate));
//    }
}
