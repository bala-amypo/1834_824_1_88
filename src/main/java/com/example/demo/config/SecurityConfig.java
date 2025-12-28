// package com.example.demo.config;

// import com.example.demo.security.JwtAuthenticationFilter;
// import com.example.demo.security.JwtTokenProvider;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     // 🔐 Authentication Manager
//     @Bean
//     public AuthenticationManager authenticationManager(
//             AuthenticationConfiguration configuration) throws Exception {
//         return configuration.getAuthenticationManager();
//     }

//     // 🔑 JWT Token Provider
//     @Bean
//     public JwtTokenProvider jwtTokenProvider() {
//         return new JwtTokenProvider(
//                 "MySuperSecretJwtKeyForApartmentSystem123456",
//                 3600000L
//         );
//     }

//     // 🔒 Password Encoder
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // 🛡 JWT Filter
//     @Bean
//     public JwtAuthenticationFilter jwtAuthenticationFilter() {
//         return new JwtAuthenticationFilter(jwtTokenProvider());
//     }

//     // 🛡️ Security Filter Chain
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//         http
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(session ->
//                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//             )
//             .httpBasic(httpBasic -> httpBasic.disable())
//             .formLogin(form -> form.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                     "/auth/**",
//                     "/health",
//                     "/swagger-ui.html",
//                     "/swagger-ui/**",
//                     "/v3/api-docs/**"
//                 ).permitAll()
//                 .anyRequest().authenticated()
//             );

//         http.addFilterBefore(jwtAuthenticationFilter(),
//                 UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }
