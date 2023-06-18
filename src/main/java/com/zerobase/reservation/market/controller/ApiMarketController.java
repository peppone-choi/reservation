package com.zerobase.reservation.market.controller;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiMarketController {
    private final MarketService marketService;

    @PostMapping("/api/market/add")
    public ResponseEntity<?> addMarket(@RequestBody MarketAdd marketAdd) {
        return marketService.addMarket(marketAdd);
    }

    @GetMapping("/api/market/reservation/{id}")
    public List<ReservationEntity> getReservation(@PathVariable long id) {
        return marketService.getReservation(id);
    }

    @PatchMapping("/api/market/reservation/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable long id) {
        return marketService.approve(id);
    }

    @DeleteMapping("/api/market/reservation/deny/{id}")
    public ResponseEntity<?> deny(@PathVariable long id) {
        return marketService.deny(id);
    }

}
