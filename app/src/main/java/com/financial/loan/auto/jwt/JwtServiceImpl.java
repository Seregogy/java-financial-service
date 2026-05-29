package com.financial.loan.auto.jwt;

import com.financial.loan.domain.service.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private static final String USER_ID = "userId";

    @Value("${JWT_SECRET}")
    private String secret;

    @Value("${JWT_EXPIRATION}")
    private Long expiration;

    public String generateToken(UUID userId) {
        return Jwts.builder()
                .claim(USER_ID, userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(
                Jwts.parser()
                        .verifyWith(getSignKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .get(USER_ID, String.class)
        );
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
