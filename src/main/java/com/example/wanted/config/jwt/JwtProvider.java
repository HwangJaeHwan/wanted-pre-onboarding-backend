package com.example.wanted.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;


    public String getToken(Long userId) {

        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getJwtKey());
        Date now = new Date(System.currentTimeMillis());
        Date expiry = new Date(System.currentTimeMillis() + 1800000);
        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();

        return token;
    }

    public Long parseToken(String token) {

        String subject = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getJwtKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }


}
