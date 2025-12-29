package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long validityInMillis = 60 * 60 * 1000; // 1 hour

    // -------------------------------------------------
    // MAIN TOKEN GENERATOR (USED BY CONTROLLER + TESTS)
    // -------------------------------------------------
    public String generateToken(
            Authentication authentication,
            Long userId,
            String email,
            String role
    ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                // ✅ REQUIRED BY t50: fallback subject = userId
                .setSubject(userId != null ? String.valueOf(userId) : email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    // -------------------------------------------------
    // TOKEN VALIDATION
    // -------------------------------------------------
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // -------------------------------------------------
    // CLAIM HELPERS
    // -------------------------------------------------
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object id = claims.get("userId");
        if (id == null) {
            return Long.valueOf(claims.getSubject()); // ✅ fallback (t50)
        }
        return Long.valueOf(id.toString());
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
