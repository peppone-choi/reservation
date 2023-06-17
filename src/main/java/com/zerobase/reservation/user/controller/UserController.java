package com.zerobase.reservation.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.exception.ExistsEmailException;
import com.zerobase.reservation.user.exception.PasswordNotMatchException;
import com.zerobase.reservation.user.exception.UserNotFoundException;
import com.zerobase.reservation.user.model.ResponseError;
import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import com.zerobase.reservation.user.model.UserLoginToken;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.util.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    /**
     * 패스워드 암호화 메소드
     * @param password 암호화 할 패스워드
     * @return 복호화 된 패스워드 출력
     */
    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * 일반 유저 회원가입 API
     * @param userInput 을 통하여 해당하는 데이터를 받아와 회원이 기존재 할 경우 예외 발생 시키고 존재하지 않을 경우 회원을 등록
     */
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
                .userName(userInput.getUserName())
                .partner("user")
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 파트너 회원가입 API
     * @param userInput 을 통하여 해당하는 데이터를 받아와 회원이 기존재 할 경우 예외 발생 시키고 존재하지 않을 경우 회원을 등록
     * 이 때 DB의 파트너 여부 컬럼에 "partner"라는 문구가 추가됨으로써 파트너 회원으로 따로 등록함
     */
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
                .userName(userInput.getUserName())
                .partner("partner")
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 로그인 시 토큰 작성 API
     * @param userLogin 을 통해 아이디와 비밀번호를 넘겨주면, 유저가 존재하고, 비밀번호가 맞는지 확인 후 토큰 생성.
     * @return 으로 JWT 토큰 발급
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
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
                .withClaim("user_id", user.getId())
                .withClaim("partner", user.getPartner())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("geronimo".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }

}
