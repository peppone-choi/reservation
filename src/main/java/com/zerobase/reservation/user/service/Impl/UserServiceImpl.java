package com.zerobase.reservation.user.service.Impl;

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
import com.zerobase.reservation.user.service.UserService;
import com.zerobase.reservation.util.PasswordUtils;
import com.zerobase.reservation.util.ResponseMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

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

    @Override
    public ResponseEntity getToken(UserLogin userLogin, Errors errors) {
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

    @Override
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("geronimo")
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.get("user_id", String.class);
            String partner = claims.get("partner", String.class);
            String username = claims.getSubject();
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + partner.toUpperCase()));

            return new UsernamePasswordAuthenticationToken(userId, null, authorities);
        } catch (ExpiredJwtException e) {
            // Handle expired token exception
        } catch (Exception e) {
            // Handle other token-related exceptions
        }

        return null; // Return null if token is invalid or parsing fails
    }

}
