package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // ✅ DEFAULT VALUES (tests rely on no-arg constructor)
    private final String jwtSecret = "MyVerySecureJwtSecretKeyForTests123456";
    private final long jwtExpirationMs = 86400000; // 1 day

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // --------------------------------------------------
    // TOKEN GENERATION
    // --------------------------------------------------

    // ✅ BASIC TOKEN (used by some tests)
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ FULL TOKEN (used by AuthController + tests)
    public String generateToken(
            Authentication authentication,
            Long userId,
            String email,
            String role
    ) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // IMPORTANT FOR FALLBACK
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --------------------------------------------------
    // TOKEN VALIDATION
    // --------------------------------------------------

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // --------------------------------------------------
    // CLAIM EXTRACTION
    // --------------------------------------------------

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ REQUIRED BY t50_jwtUserIdFallbackSubject
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);

        Object id = claims.get("userId");
        if (id != null) {
            return Long.valueOf(id.toString());
        }

        // 🔥 FALLBACK TO SUBJECT (TEST REQUIREMENT)
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        Object role = getClaims(token).get("role");
        return role == null ? null : role.toString();
    }

    public String getEmailFromToken(String token) {
        Object email = getClaims(token).get("email");
        return email == null ? null : email.toString();
    }
}
