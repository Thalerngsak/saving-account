package com.example.saving.account.security;

import com.example.saving.account.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final KeyPair keyPair;

    public JwtService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String generateToken(String email, UserRole role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
