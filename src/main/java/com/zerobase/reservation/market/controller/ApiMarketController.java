package com.zerobase.reservation.market.controller;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.service.MarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "매장 관리자 API")
public class ApiMarketController {
    private final MarketService marketService;

    /**
     * 매장 추가 메소드
     * @param marketAdd
     * @return
     */
    @ApiOperation(value = "매장 추가 API")
    @PostMapping("/api/market/add")
    public ResponseEntity<?> addMarket(@RequestBody MarketAdd marketAdd) {
        return marketService.addMarket(marketAdd);
    }

    /**
     * 매장 별 주문 현황 확인 메소드
     * @param id
     * @return
     */
    @ApiOperation(value = "매장 별 주문 현황 확인 API")
    @GetMapping("/api/market/reservation/{id}")
    public List<ReservationEntity> getReservation(@PathVariable long id) {
        return marketService.getReservation(id);
    }

    /**
     * 주문 승인 메소드
     * @param id
     * @return
     */
    @ApiOperation(value = "주문 승인 API")
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
    @ApiOperation(value = "주문 거절 API")
    @DeleteMapping("/api/market/reservation/deny/{id}")
    public ResponseEntity<?> deny(@PathVariable long id) {
        return marketService.deny(id);
    }

}
