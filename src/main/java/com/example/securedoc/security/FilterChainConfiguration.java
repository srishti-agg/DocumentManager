package com.example.securedoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
        //This return allows any user without authentication to access the path request mentioned in it
        return security
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(request->
                        request
                                .requestMatchers("/user/login")
                                .authenticated()
                                .requestMatchers("/user/verify/account").permitAll()

                                )
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){

//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//
//        return new ProviderManager(authenticationProvider);
        var myOwnAuthenticationProvider = new MyOwnAuthenticationProvider(userDetailsService);
        return new ProviderManager(myOwnAuthenticationProvider);
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        var junior = User.withDefaultPasswordEncoder()
//                .username("junior")
//                .password("{noop}letmein")
//                .roles("USER")
//                .build();
//        var hanna = User.withDefaultPasswordEncoder()
//                .username("hanna")
//                .password("{noop}letmein")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(List.of(junior,hanna));
//    }

    @Bean
    public UserDetailsService inMemoryDetailsManager(){

        return new InMemoryUserDetailsManager(
                User.withUsername("hanna")
                        .password("letmein")
                        .roles("USER").build(),
                User.withUsername("junior")
                        .password("letmein")
                        .roles("USER")
                        .build()
        );
    }
}
