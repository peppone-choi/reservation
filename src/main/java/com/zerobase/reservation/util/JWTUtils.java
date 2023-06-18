package com.zerobase.reservation.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JWTUtils {

    private static final String KEY = "geronimo";

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        byte[] secretKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(secretKeyBytes);

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

    }

    public long getExpirationMs() {
        long expirationMs = 3000;
        return expirationMs;
    }

    public String getSecretKey() {
        return KEY;
    }
}
