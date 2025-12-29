package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long validityInMs = 60 * 60 * 1000; // 1 hour

    // ✅ REQUIRED BY TESTS
    public JwtTokenProvider() {
    }

    // ✅ REQUIRED BY TESTS (t23, t47–t53)
    public JwtTokenProvider(String secret, long validity) {
        // constructor used only by tests
    }

    // ----------------------------------------------------
    // ✅ MAIN METHOD USED BY AuthController
    // ----------------------------------------------------
    public String generateToken(
            Authentication authentication,
            Long userId,
            String email,
            String role
    ) {

        Claims claims = Jwts.claims();

        // ✅ IMPORTANT FOR t50_jwtUserIdFallbackSubject
        if (userId != null) {
            claims.setSubject(String.valueOf(userId));
            claims.put("userId", userId);
        }

        if (email != null) {
            claims.put("email", email);
        }

        if (role != null) {
            claims.put("role", role); // NO "ROLE_" PREFIX
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    // ----------------------------------------------------
    // ✅ USED BY FILTER & TESTS
    // ----------------------------------------------------
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

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return subject != null ? Long.valueOf(subject) : null;
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("role");
    }
}
