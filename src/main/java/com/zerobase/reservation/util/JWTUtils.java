package com.zerobase.reservation.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JWTUtils {

    private static final String KEY = "geronimo";

    /**
     * 토큰 생성 메소드
     * @param token
     * @return
     */
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

    /**
     * 토큰에서 claim 추출
     * @param token
     * @return
     */
    public Claims extractClaims(String token) {
        byte[] secretKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(secretKeyBytes);

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

    }


    public long getExpirationMs() {
        long expirationMs = 86400000; // 하루
        return expirationMs;
    }

    public String getSecretKey() {
        return KEY;
    }
}
