package com.example.checkin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // disable CSRF for Postman testing
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/visitors/**").permitAll() // allow all API calls for testing
                    .anyRequest().authenticated() // secure all other endpoints
            )
            .httpBasic(Customizer.withDefaults()) // use basic auth if needed
            .build();
    }
}
