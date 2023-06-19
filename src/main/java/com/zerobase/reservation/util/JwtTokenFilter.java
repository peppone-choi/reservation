package com.zerobase.reservation.util;

import com.zerobase.reservation.user.model.Auth;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends UsernamePasswordAuthenticationFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * 인증 시도 메소드
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String token = extractToken(request);

        if (token != null && jwtUtils.validateToken(token)) {
            Claims claims = jwtUtils.extractClaims(token);
            Auth role = Auth.valueOf(claims.get("partner", String.class));

            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

            Collection<? extends GrantedAuthority> authorities;
            if (role != null) {
                authorities = Collections.singleton(new SimpleGrantedAuthority(role.name()));
            } else {
                authorities = Collections.emptyList();
            }

            response.addHeader("Authorization", "Bearer " + token);

            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        }

        throw new AccessDeniedException("접속 불가");
    }

    /**
     * 인증 성공 시 권한 발급
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        System.out.println("부여된 역할(Role): " + authorities);
        chain.doFilter(request, response);
    }

    /**
     * 토큰에서 인증 정보 추출 메소드
     * @param request
     * @return
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
