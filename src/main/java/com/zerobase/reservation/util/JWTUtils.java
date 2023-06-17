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
            byte[] secretKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
            Key key = Keys.hmacShaKeyFor(secretKeyBytes);

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 유효하지 않은 토큰 또는 파싱 오류인 경우 false를 반환
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
}
