package com.zerobase.reservation.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddReservation {
    private long marketId;
    private LocalDateTime reserveDate;
    private LocalDateTime reserveTime;
    private String reservationPhone;
}
