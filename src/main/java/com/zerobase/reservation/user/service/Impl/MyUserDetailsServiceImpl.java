package com.zerobase.reservation.user.service.Impl;

import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.repository.UserRepository;
import com.zerobase.reservation.user.service.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
