package com.zerobase.reservation.user.controller;

import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.exception.InvalidTokenException;
import com.zerobase.reservation.user.exception.PasswordNotMatchException;
import com.zerobase.reservation.user.exception.TokenExpiredException;
import com.zerobase.reservation.user.model.*;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.user.service.UserService;
import com.zerobase.reservation.util.JWTUtils;
import com.zerobase.reservation.util.PasswordUtils;
import com.zerobase.reservation.util.ResponseMessage;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 일반 유저 회원가입 API
     */
    @PostMapping("/api/user/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserInput userInput, Errors errors) {
        ResponseEntity register = userService.setRegister(userInput, errors);
        return ResponseEntity.ok().body(ResponseMessage.success(register));
    }

    /**
     * 파트너 회원가입 API
     */
    @PostMapping("/api/user/register/partner")
    public ResponseEntity<?> registerPartner(@RequestBody @Valid UserInput userInput, Errors errors) {
        ResponseEntity partnerRegister = userService.setPartnerRegister(userInput, errors);
        return ResponseEntity.ok().body(ResponseMessage.success(partnerRegister));
    }

    /**
     * 로그인 API
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        ResponseEntity loginRegister = userService.login(userLogin, errors);
        return ResponseEntity.ok().body(ResponseMessage.success(loginRegister));
    }


}
