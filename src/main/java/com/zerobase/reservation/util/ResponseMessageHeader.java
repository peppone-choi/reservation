package com.zerobase.reservation.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageHeader {

    private boolean result;
    private String resultCode;
    private String message;
    private int status;
}
