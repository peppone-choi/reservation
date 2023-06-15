package com.zerobase.reservation.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTUtils {

    private static final String KEY = "zerobase";

    public static String getIssuer(String token) {
        String issuer = JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build().verify(token)
                .getIssuer();

        return issuer;
    }

}
