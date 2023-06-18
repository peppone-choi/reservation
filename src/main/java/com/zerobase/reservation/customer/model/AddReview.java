package com.zerobase.reservation.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddReview {
    @NotBlank
    private Long marketId;
    @NotBlank
    private String reviewSubject;
    @NotBlank
    private String reviewDesc;
}
