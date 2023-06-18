package com.zerobase.reservation.user.service.Impl;

import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.model.LoginResponse;
import com.zerobase.reservation.user.model.ResponseError;
import com.zerobase.reservation.user.model.UserLogin;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.user.service.MyUserDetailService;
import com.zerobase.reservation.util.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 이메일이 없습니다."));

        String partnerRole = "ROLE_" + user.getPartner().toUpperCase();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(partnerRole));

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


}
