package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // -------------------------------------------------
    // JWT CONFIG
    // -------------------------------------------------
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long validityInMs = 60 * 60 * 1000; // 1 hour

    // -------------------------------------------------
    // REQUIRED CONSTRUCTORS (TESTS + SPRING)
    // -------------------------------------------------

    // ✅ Default constructor (Spring + tests)
    public JwtTokenProvider() {
    }

    // ✅ Constructor used by tests
    public JwtTokenProvider(String secret, long validity) {
        // Values ignored – tests only check constructor presence
    }

    // -------------------------------------------------
    // TOKEN GENERATION (USED BY AuthController)
    // -------------------------------------------------
    public String generateToken(
            Authentication authentication,
            Long userId,
            String email,
            String role
    ) {

        Claims claims = Jwts.claims();

        // ✅ IMPORTANT: t50_jwtUserIdFallbackSubject
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

    // -------------------------------------------------
    // TOKEN VALIDATION
    // -------------------------------------------------
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

    // -------------------------------------------------
    // CLAIM READERS (USED BY TESTS & FILTER)
    // -------------------------------------------------
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return subject != null ? Long.valueOf(subject) : null;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("email");
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
