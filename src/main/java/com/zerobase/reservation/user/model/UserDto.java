package com.zerobase.reservation.user.model;

import com.zerobase.reservation.user.entity.UserEntity;
import com.zerobase.reservation.user.model.constants.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private Authority partner;
}
