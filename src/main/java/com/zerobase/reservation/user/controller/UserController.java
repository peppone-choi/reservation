package com.zerobase.reservation.user.controller;

import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import com.zerobase.reservation.user.model.UserLoginToken;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.user.service.UserService;
import com.zerobase.reservation.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;


    /**
     * 일반 유저 회원가입 API
     * @param userInput 을 통하여 해당하는 데이터를 받아와 회원이 기존재 할 경우 예외 발생 시키고 존재하지 않을 경우 회원을 등록
     */
    @PostMapping("/api/user/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserInput userInput, Errors errors) {
        ResponseEntity register = userService.setRegister(userInput, errors);
        return ResponseEntity.ok().body(ResponseMessage.success(register));
    }

    /**
     * 파트너 회원가입 API
     * @param userInput 을 통하여 해당하는 데이터를 받아와 회원이 기존재 할 경우 예외 발생 시키고 존재하지 않을 경우 회원을 등록
     * 이 때 DB의 파트너 여부 컬럼에 "partner"라는 문구가 추가됨으로써 파트너 회원으로 따로 등록함
     */
    @PostMapping("/api/user/register/partner")
    public ResponseEntity<?> registerPartner(@RequestBody @Valid UserInput userInput, Errors errors) {
        ResponseEntity partnerRegister = userService.setPartnerRegister(userInput, errors);
        return ResponseEntity.ok().body(ResponseMessage.success(partnerRegister));
    }

    /**
     * 로그인 시 토큰 작성 API
     * @param userLogin 을 통해 아이디와 비밀번호를 넘겨주면, 유저가 존재하고, 비밀번호가 맞는지 확인 후 토큰 생성.
     * @return 으로 JWT 토큰 발급
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        ResponseEntity<?> tokenResponse = userService.getToken(userLogin, errors);
        if (tokenResponse.getStatusCode() != HttpStatus.OK) {
            return tokenResponse;
        }

        String token = ((UserLoginToken) tokenResponse.getBody()).getToken();

        try {
            userService.authenticateUserWithToken(token);
            return ResponseEntity.ok().body(ResponseMessage.success("로그인이 정상적으로 되었습니다."));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseMessage.fail("부적합한 토큰입니다."));
        }
    }
}
