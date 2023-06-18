package com.zerobase.reservation.market.service;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MarketService {

    ResponseEntity<?> addMarket(MarketAdd marketAdd);

    List<ReservationEntity> getReservation(long id);

    ResponseEntity approve(long id);

    ResponseEntity<?> deny(long id);

}
