package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // -------------------------------------------------
    // PASSWORD ENCODER
    // -------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -------------------------------------------------
    // AUTHENTICATION MANAGER
    // -------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // -------------------------------------------------
    // SECURITY FILTER CHAIN
    // -------------------------------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtTokenProvider);

        http
            // Disable CSRF (JWT based)
            .csrf(csrf -> csrf.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // PUBLIC ENDPOINTS
                .requestMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                // ALL OTHER ENDPOINTS REQUIRE TOKEN
                .anyRequest().authenticated()
            )

            // JWT FILTER
            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
