package com.example.demo.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "secret-key-123456";
    private final long validityInMs = 3600000; // 1 hour

    // ✅ Used by AuthController
    public String generateToken(Long userId, String email, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("email", email);
        claims.put("role", role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ✅ Used by JwtAuthenticationFilter
    public String getEmailFromToken(String token) {
        return getClaims(token).get("email", String.class);
    }

    public Long getUserIdFromToken(String token) {
        String subject = getClaims(token).getSubject();
        return subject == null ? null : Long.parseLong(subject);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
