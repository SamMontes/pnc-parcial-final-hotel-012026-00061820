package com.uca.pncparcialfinalhotel.security;

import com.uca.pncparcialfinalhotel.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String CLAIM_TOKEN_VERSION = "tokenVersion";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateAccessToken(User user) {
        return buildToken(user, TYPE_ACCESS, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, TYPE_REFRESH, refreshTokenExpiration);
    }

    private String buildToken(User user, String tokenType, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .subject(user.getEmail())
                .claim(CLAIM_TOKEN_VERSION, user.getTokenVersion())
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(signingKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(extractClaims(token).get(CLAIM_TOKEN_TYPE, String.class));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(extractClaims(token).get(CLAIM_TOKEN_TYPE, String.class));
    }

    // Valida firma+expiracion (ya verificadas por extractClaims, que lanza
    // JwtException si algo no cuadra) y, para la Opcion A, que el token
    // siga en la version vigente del usuario (no invalidado por un cambio
    // de contrasena posterior a su emision).
    public boolean isTokenValid(String token, User user) {
        try {
            Claims claims = extractClaims(token);
            boolean sameEmail = user.getEmail().equals(claims.getSubject());
            Integer tokenVersion = claims.get(CLAIM_TOKEN_VERSION, Integer.class);
            boolean sameVersion = tokenVersion != null && tokenVersion.equals(user.getTokenVersion());
            return sameEmail && sameVersion;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}