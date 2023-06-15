package com.zerobase.reservation.user.repository;

import com.zerobase.reservation.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    int countByEmail(String email);

    Optional<Object> findByEmail(String email);
}
