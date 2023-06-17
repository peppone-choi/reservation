package com.zerobase.reservation.market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketAdd {
    @NotBlank
    private String marketName;
    @NotBlank
    private String marketDesc;
    @NotBlank
    private String marketX;
    @NotBlank
    private String marketY;
}
