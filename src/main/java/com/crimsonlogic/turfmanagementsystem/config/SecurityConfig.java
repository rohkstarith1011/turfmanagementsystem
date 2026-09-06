package com.crimsonlogic.turfmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/users/**",
                    "/api/user-roles/**",
                    "/api/admins/**",
                    "/api/sports/**",
                    "/api/amenities/**",
                    "/api/turf-owners/**",
                    "/api/facilities/**",
                    "/api/turf-managers/**",
                    "/api/turf-sports/**",
                    "/api/turf-amenities/**",
                    "/api/playing-areas/**",
                    "/api/slot-blocks/**",
                    "/api/slots/**",
                    "/api/players/**",
                    "/api/teams/**",
                    "/api/team-players/**",
                    "/api/coaches/**",
                    "/api/coaching-classes/**",
                    "/api/coaching-class-registrations/**",
                    "/api/bookings/**",
                    "/api/booking-players/**",
                    "/api/payments/**",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}