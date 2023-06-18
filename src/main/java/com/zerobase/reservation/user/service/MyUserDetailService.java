package com.zerobase.reservation.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyUserDetailService extends UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
