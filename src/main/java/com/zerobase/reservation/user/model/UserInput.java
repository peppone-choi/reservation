package com.zerobase.reservation.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInput {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}
