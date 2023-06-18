package com.zerobase.reservation.user.service;

import com.zerobase.reservation.user.model.UserInput;
import com.zerobase.reservation.user.model.UserLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;

public interface UserService {
    ResponseEntity<?> setRegister(UserInput userInput, Errors errors);

    ResponseEntity setPartnerRegister(UserInput userInput, Errors errors);

    ResponseEntity<?> getToken(UserLogin userLogin, Errors errors);

    Authentication authenticateUserWithToken(String token);
}
