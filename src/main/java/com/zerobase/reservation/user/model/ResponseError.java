package com.zerobase.reservation.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {
    private String field;
    private String message;

    public static ResponseError of(FieldError e) {
        return ResponseError.builder().field((e).getField())
                .message(e.getDefaultMessage())
                .build();
    }
}