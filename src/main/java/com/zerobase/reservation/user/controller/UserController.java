package com.zerobase.reservation.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.exception.ExistsEmailException;
import com.zerobase.reservation.user.exception.PasswordNotMatchException;
import com.zerobase.reservation.user.exception.UserNotFoundException;
import com.zerobase.reservation.user.model.ResponseError;
import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLoginToken;
import com.zerobase.reservation.user.model.constants.Authority;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.util.PasswordUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        UserEntity user = UserEntity.builder()
                .email(userInput.getEmail())
                .password(encryptPassword)
                .partner(Authority.ROLE_MEMBER)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/user/register/partner")
    public ResponseEntity<?> registerPartner(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        UserEntity user = UserEntity.builder()
                .email(userInput.getEmail())
                .password(encryptPassword)
                .partner(Authority.ROLE_PARTNER)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserInput userLogin, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        UserEntity user = (UserEntity) userRepository.findByEmail(userLogin.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);

        Date expairedDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String token = JWT.create()
                .withExpiresAt(expairedDate)
                .withClaim("user_id", user.getEmail())
                .withClaim("role", user.getPartner().toString())
                .withSubject(user.getEmail())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("geronimo".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }
}
