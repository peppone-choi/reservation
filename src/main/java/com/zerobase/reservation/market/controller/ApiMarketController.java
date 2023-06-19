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

    /**
     * 매장 추가 메소드
     * @param marketAdd
     * @return
     */
    @PostMapping("/api/market/add")
    public ResponseEntity<?> addMarket(@RequestBody MarketAdd marketAdd) {
        return marketService.addMarket(marketAdd);
    }

    /**
     * 매장 별 주문 현황 확인 메소드
     * @param id
     * @return
     */
    @GetMapping("/api/market/reservation/{id}")
    public List<ReservationEntity> getReservation(@PathVariable long id) {
        return marketService.getReservation(id);
    }

    /**
     * 주문 승인 메소드
     * @param id
     * @return
     */
    @PatchMapping("/api/market/reservation/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable long id) {
        return marketService.approve(id);
    }

    /**
     * 주문 거절 메소드
     * 주문 거절이 되면 해당 주문은 삭제된다.
     * @param id
     * @return
     */
    @DeleteMapping("/api/market/reservation/deny/{id}")
    public ResponseEntity<?> deny(@PathVariable long id) {
        return marketService.deny(id);
    }

}
