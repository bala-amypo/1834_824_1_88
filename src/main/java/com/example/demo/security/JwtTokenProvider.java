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

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    // -------------------------------------------------
    // TOKEN GENERATION
    // -------------------------------------------------
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))          // ✅ subject = userId
                .claim("userId", userId)                     // ✅ explicit userId
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // -------------------------------------------------
    // VALIDATION
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
    // AUTHENTICATION (USED BY FILTER)
    // -------------------------------------------------
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String role = getRoleFromToken(token);

        List<SimpleGrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),   // principal
                null,
                authorities
        );
    }

    // -------------------------------------------------
    // CLAIM HELPERS (REQUIRED BY FILTER & TESTS)
    // -------------------------------------------------
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);

        Long userId = claims.get("userId", Long.class);

        // ✅ fallback to subject if claim missing
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

    // -------------------------------------------------
    // INTERNAL
    // -------------------------------------------------
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
