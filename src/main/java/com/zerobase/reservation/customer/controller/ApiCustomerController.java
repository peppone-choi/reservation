package com.zerobase.reservation.customer.controller;

import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.customer.service.CustomerService;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiCustomerController {
    private final CustomerService customerService;

    @GetMapping("/api/customer/")
    public List<MarketEntity> getMarketList(@RequestBody String name) {
        return customerService.getMarketList(name);
    }

    @GetMapping("/api/customer/{id}")
    public MarketEntity getMarketInfo(@PathVariable long id) {
        return customerService.getMarketInfo(id);
    }

    @PostMapping("/api/customer/reservation")
    public ResponseEntity<?> addReservation(@RequestBody AddReservation addReservation) {
        return customerService.addReservation(addReservation);
    }



}
