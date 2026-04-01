package com.example.market_mate.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String secretKey =
            "MARKETMATE_JWT_SUPER_SECRET_KEY_2025_ABCDEFG";

    private final long accessTokenValidity = 1000L * 60 * 30;           // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    private final Key key =
            Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public String createAccessToken(Long userId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("roles", List.of(role))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        User principal = new User(userId, "", authorities);

        return new UsernamePasswordAuthenticationToken(
                principal, token, authorities);
    }
}