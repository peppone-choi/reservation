package com.zerobase.reservation.user.service.Impl;

import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.exception.*;
import com.zerobase.reservation.user.model.LoginResponse;
import com.zerobase.reservation.user.model.ResponseError;
import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.user.service.UserService;
import com.zerobase.reservation.util.JWTUtils;
import com.zerobase.reservation.util.PasswordUtils;
import com.zerobase.reservation.util.ResponseMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public ResponseEntity setRegister(UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속 제한이 해제된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        UserEntity user = UserEntity.builder()
                .email(userInput.getEmail())
                .password(encryptPassword)
                .userName(userInput.getUserName())
                .partner("user")
                .build();

        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @Override
    public ResponseEntity setPartnerRegister(UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속 제한이 해제된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        UserEntity user = UserEntity.builder()
                .email(userInput.getEmail())
                .password(encryptPassword)
                .userName(userInput.getUserName())
                .partner("partner")
                .build();

        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<?> login(UserLogin userLogin, Errors errors) {
        if (errors.hasErrors()) {
            List<ResponseError> responseErrorList = errors.getAllErrors()
                    .stream()
                    .map(error -> ResponseError.of((FieldError) error))
                    .collect(Collectors.toList());
            LoginResponse loginResponse = new LoginResponse(responseErrorList.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword())
            );

            // 인증 성공 처리
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserEntity user = userRepository.findByEmail(userLogin.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 사용자를 찾을 수 없습니다: " + userLogin.getEmail()));

            String token = generateToken(user);
            LoginResponse loginResponse = new LoginResponse("로그인이 정상적으로 되었습니다.");
            HttpHeaders headers = createAuthorizationHeader(token, user.getPartner());
            return ResponseEntity.ok().headers(headers).body(loginResponse);

        } catch (AuthenticationException e) {
            // 인증 실패 처리
            LoginResponse loginResponse = new LoginResponse("인증에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    private String generateToken(UserEntity user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtUtils.getExpirationMs());

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId());
        claims.put("roles", user.getPartner());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtUtils.getSecretKey())
                .compact();
    }

    private HttpHeaders createAuthorizationHeader(String token, String partner) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + token);
        headers.add("partner", partner);
        return headers;
    }

}
