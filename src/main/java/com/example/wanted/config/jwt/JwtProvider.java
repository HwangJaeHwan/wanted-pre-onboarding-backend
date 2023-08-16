package com.example.wanted.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;


    public String getAccessToken(Long userId) {

        return "Bearer " + makeToken(userId, 1800000);
    }

    public String getRefreshToken(Long userId) {

        return "Bearer " + makeToken(userId, 86400000);
    }

    public Long parseToken(String token) {

        token = token.substring(7);


        log.info("token = {}", token);

        String subject = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getJwtKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }

    private String makeToken(Long userId,long time) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getJwtKey());
        Date now = new Date(System.currentTimeMillis());
        Date expiry = new Date(System.currentTimeMillis() + time);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }


}
