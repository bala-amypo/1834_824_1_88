package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Key key;
    private long validityInMs;

    // -------------------------------------------------
    // ✅ NO-ARG CONSTRUCTOR (SPRING + TESTS)
    // -------------------------------------------------
    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(
                "DefaultJwtSecretKeyForSpringBootTests123456".getBytes()
        );
        this.validityInMs = 3600000; // 1 hour
    }

    // -------------------------------------------------
    // ✅ REQUIRED BY TESTS
    // new JwtTokenProvider("secret", 3600000L)
    // -------------------------------------------------
    public JwtTokenProvider(String secret, long validityInMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMs = validityInMs;
    }

    // -------------------------------------------------
    // TOKEN GENERATION
    // -------------------------------------------------
    public String generateToken(
            Authentication authentication,
            Long userId,
            String email,
            String role
    ) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // IMPORTANT
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------------------------------------
    // TOKEN VALIDATION
    // -------------------------------------------------
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // -------------------------------------------------
    // CLAIM HELPERS
    // -------------------------------------------------
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // -------------------------------------------------
    // ✅ t50_jwtUserIdFallbackSubject FIX
    // -------------------------------------------------
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);

        Object id = claims.get("userId");
        if (id != null) {
            return Long.valueOf(id.toString());
        }

        // fallback to subject
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        Object email = getClaims(token).get("email");
        return email == null ? null : email.toString();
    }

    public String getRoleFromToken(String token) {
        Object role = getClaims(token).get("role");
        return role == null ? null : role.toString();
    }
}
