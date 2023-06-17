package com.zerobase.reservation.market.controller;

import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiMarketController {
    private final MarketService marketService;

    @PostMapping("/api/market/add")
    public ResponseEntity<?> addMarket(@RequestBody MarketAdd marketAdd) {

        return ResponseEntity.ok().body("매장 등록이 정상 처리되었습니다.");
    }



}
