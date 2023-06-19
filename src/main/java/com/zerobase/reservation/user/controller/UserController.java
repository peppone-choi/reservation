package com.zerobase.reservation.user.controller;

import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import com.zerobase.reservation.user.service.UserService;
import com.zerobase.reservation.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
