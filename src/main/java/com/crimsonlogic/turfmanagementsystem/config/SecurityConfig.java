package com.crimsonlogic.turfmanagementsystem.config;

import com.crimsonlogic.turfmanagementsystem.security.JwtAccessDeniedHandler;
import com.crimsonlogic.turfmanagementsystem.security.JwtAuthenticationEntryPoint;
import com.crimsonlogic.turfmanagementsystem.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          JwtAuthenticationEntryPoint jwtAuthEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Registration
                .requestMatchers(HttpMethod.GET, "/api/sports/**", "/api/amenities/**", "/api/facilities/**").permitAll()
                
                // Dashboards
                .requestMatchers("/api/admin-dashboard/**").hasRole("ADMIN")
                .requestMatchers("/api/turf-owner-dashboard/**").hasRole("OWNER")
                
                // User & Roles
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "PLAYER", "OWNER", "MANAGER", "COACH")
                .requestMatchers("/api/user-roles/**").hasRole("ADMIN")
                .requestMatchers("/api/admins/**").hasRole("ADMIN")
                
                // Management endpoints
                .requestMatchers(HttpMethod.POST, "/api/sports/**", "/api/amenities/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/sports/**", "/api/amenities/**").hasRole("ADMIN")
                
                // Owner/Facility mgmt
                .requestMatchers("/api/turf-owners/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/facilities/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT, "/api/facilities/**").hasRole("OWNER")
                
                .requestMatchers("/api/playing-areas/**").hasRole("OWNER")
                .requestMatchers("/api/slot-blocks/**").hasRole("OWNER")
                .requestMatchers("/api/slots/**").hasAnyRole("OWNER", "PLAYER")
                
                // Coach & Coaching Classes
                .requestMatchers(HttpMethod.GET, "/api/coaches/**", "/api/coaching-classes/**", "/api/coaching-class-registrations/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/coaches/**", "/api/coaching-classes/**").hasAnyRole("COACH", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/coaches/**", "/api/coaching-classes/**").hasAnyRole("COACH", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/coaches/**", "/api/coaching-classes/**").hasAnyRole("COACH", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/coaching-class-registrations/**").hasAnyRole("PLAYER", "COACH", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/coaching-class-registrations/**").hasAnyRole("COACH", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/coaching-class-registrations/**").hasAnyRole("COACH", "ADMIN")
                
                .requestMatchers("/api/bookings/**").hasAnyRole("PLAYER", "OWNER", "ADMIN")
                .requestMatchers("/api/payments/**").hasAnyRole("PLAYER", "OWNER", "ADMIN")
                .requestMatchers("/api/reviews/**").hasAnyRole("PLAYER", "OWNER", "ADMIN")
                
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}