package com.zerobase.reservation.user.service;

import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

public interface UserService {
    ResponseEntity<?> setRegister(UserInput userInput, Errors errors);

    ResponseEntity setPartnerRegister(UserInput userInput, Errors errors);

    ResponseEntity<?> login(UserLogin userLogin, Errors errors);

}
