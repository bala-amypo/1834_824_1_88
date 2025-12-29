package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private Key key;
    private long expiration;

    // ✅ REQUIRED BY SPRING
    public JwtTokenProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expiration = 1000 * 60 * 60; // 1 hour
    }

    // ✅ REQUIRED BY TESTS
    public JwtTokenProvider(String secret, long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // ✅ USED BY APPLICATION
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // ✅ REQUIRED BY TESTS
    public String generateToken(
            Authentication auth,
            long userId,
            String email,
            String role
    ) {
        return generateToken(userId, email, role);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String role = claims.get("role", String.class);
        List<SimpleGrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities
        );
    }

    // ✅ REQUIRED BY TESTS
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        Long userId = claims.get("userId", Long.class);

        if (userId == null) {
            return Long.parseLong(claims.getSubject());
        }
        return userId;
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
