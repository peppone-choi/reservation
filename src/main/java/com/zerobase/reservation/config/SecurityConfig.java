package com.zerobase.reservation.config;

import com.zerobase.reservation.util.JWTUtils;
import com.zerobase.reservation.util.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/market/**").hasRole("partner")
                .antMatchers("/api/customer/**").hasAnyRole("user", "partner")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, ex) -> {
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
                })
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtUtils, userDetailsService), UsernamePasswordAuthenticationFilter.class);

    }

}